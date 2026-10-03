package br.com.carlos.Owl.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.carlos.Owl.dto.infra.response.BookResponse;
import br.com.carlos.Owl.dto.infra.response.LoanResponse;
import br.com.carlos.Owl.dto.infra.response.StudentResponse;
import br.com.carlos.Owl.entity.Book;
import br.com.carlos.Owl.entity.Loan;
import br.com.carlos.Owl.entity.Student;
import br.com.carlos.Owl.enums.LoanStatus;
import br.com.carlos.Owl.exception.Books.BookNotFoundException;
import br.com.carlos.Owl.exception.Books.BookUnavailableException;
import br.com.carlos.Owl.exception.Loans.LoanNotFoundException;
import br.com.carlos.Owl.exception.Students.StudentHasActiveLoanException;
import br.com.carlos.Owl.exception.Students.StudentNotFoundException;
import br.com.carlos.Owl.repository.BookRepository;
import br.com.carlos.Owl.repository.LoanRepository;
import br.com.carlos.Owl.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/** Applies business rules for creating, returning, and querying book loans. */
@Service
@RequiredArgsConstructor
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;

    private LoanResponse toLoanResponse(Loan loan) {
        return new LoanResponse(
                new StudentResponse(loan.getStudent().getName(), loan.getStudent().getRegistrationNumber()),
                new BookResponse(loan.getBook().getTitle(), loan.getBook().getAuthor(), loan.getBook().getIsbn(),
                        loan.getBook().getCategory(), loan.getBook().isAvailable()),
                loan.getLoanDate().toString(), loan.getDueDate().toString(), loan.getStatus().name());
    }

    /**
     * Creates a loan for a student.
     *
     * @param registrationNumber Registration number of the student borrowing the book.
     * @param isbn ISBN of the book to be borrowed.
     * @return The created loan.
     * @throws StudentNotFoundException If the student is not found.
     * @throws BookNotFoundException If the book is not found.
     * @throws BookUnavailableException If the book is unavailable.
     * @throws StudentHasActiveLoanException If the student already has an active loan.
     */
    @Transactional
    public LoanResponse createLoan(String registrationNumber, String isbn) {

        Student student = studentRepository.findByRegistrationNumber(registrationNumber);

        if (student == null) {
            throw new StudentNotFoundException();
        }

        Book book = bookRepository.findByIsbn(isbn).orElseThrow(BookNotFoundException::new);

        if (!book.isAvailable()) {
            throw new BookUnavailableException();
        }

        if (loanRepository.existsByStudentRegistrationNumberAndStatus(registrationNumber, LoanStatus.ACTIVE)) {
            throw new StudentHasActiveLoanException();
        }

        Loan loan = Loan.builder().student(student).book(book).loanDate(LocalDate.now())
                .dueDate(LocalDate.now().plusMonths(1)).status(LoanStatus.ACTIVE).build();

        book.setAvailable(false);
        bookRepository.save(book);

        Loan savedLoan = loanRepository.save(loan);

        return toLoanResponse(savedLoan);
    }

    /**
     * Returns an active loan.
     *
     * @param registrationNumber Registration number of the student returning the book.
     * @return The returned loan.
     * @throws LoanNotFoundException If no active loan is found for the student.
     */
    @Transactional
    public LoanResponse returnLoan(String registrationNumber) {

        Loan loan = loanRepository.findByStudentRegistrationNumberAndStatus(registrationNumber, LoanStatus.ACTIVE)
                .orElseThrow(LoanNotFoundException::new);

        loan.setReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);

        Book book = loan.getBook();
        book.setAvailable(true);

        bookRepository.save(book);

        return toLoanResponse(loanRepository.save(loan));
    }

    /**
     * Lists all loans.
     *
     * @return A list of loans.
     */
    public List<LoanResponse> list() {
        return loanRepository.findAll().stream().map(this::toLoanResponse).toList();
    }

    /**
     * Finds loans by status.
     *
     * @param status Loan status.
     * @return A list of loans with the given status.
     */
    public List<LoanResponse> findByStatus(LoanStatus status) {
        return loanRepository.findByStatus(status).stream().map(this::toLoanResponse).toList();
    }

    /**
     * Finds loans by student registration number.
     *
     * @param registrationNumber Registration number of the student.
     * @return A list of the student's loans.
     */
    public List<LoanResponse> findByStudentRegistrationNumber(String registrationNumber) {
        return loanRepository.findByStudentRegistrationNumber(registrationNumber).stream().map(this::toLoanResponse)
                .toList();
    }

    /**
     * Checks whether the student has an active loan.
     *
     * @param registrationNumber Registration number of the student.
     * @return true if the student has an active loan.
     */
    public boolean hasActiveLoan(String registrationNumber) {
        return loanRepository.existsByStudentRegistrationNumberAndStatus(registrationNumber, LoanStatus.ACTIVE);
    }
}
