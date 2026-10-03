package br.com.carlos.Owl.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.carlos.Owl.dto.infra.response.LoanResponse;
import br.com.carlos.Owl.entity.Book;
import br.com.carlos.Owl.entity.Loan;
import br.com.carlos.Owl.entity.Student;
import br.com.carlos.Owl.enums.LoanStatus;
import br.com.carlos.Owl.exception.Books.BookNotFoundException;
import br.com.carlos.Owl.exception.Books.BookUnavailableException;
import br.com.carlos.Owl.exception.Loans.LoanNotFoundException;
import br.com.carlos.Owl.exception.Students.StudentNotFoundException;
import br.com.carlos.Owl.repository.BookRepository;
import br.com.carlos.Owl.repository.LoanRepository;
import br.com.carlos.Owl.repository.StudentRepository;
import br.com.carlos.Owl.service.LoanService;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @InjectMocks
    private LoanService loanService;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private StudentRepository studentRepository;

    //==================================================
    // TESTS - CREATE LOAN
    //==================================================

    // Success
    @Test
    void createLoanSuccessfully() {

        Book book = new Book();
        Student student = new Student();

        student.setName("Carlos");
        student.setRegistrationNumber("123456");

        book.setTitle("The Lord of the Rings");
        book.setIsbn("9780261102394");

        when(studentRepository.findByRegistrationNumber("123456")).thenReturn(student);

        when(bookRepository.findByIsbn("9780261102394")).thenReturn(Optional.of(book));

        when(loanRepository.existsByStudentRegistrationNumberAndStatus("123456", LoanStatus.ACTIVE)).thenReturn(false);

        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LoanResponse result = loanService.createLoan("123456", "9780261102394");

        assertNotNull(result);

        assertEquals("Carlos", result.student().name());
        assertEquals("123456", result.student().registrationNumber());

        assertEquals("The Lord of the Rings", result.book().title());

        assertEquals("9780261102394", result.book().isbn());

        assertFalse(book.isAvailable());

        verify(studentRepository).findByRegistrationNumber("123456");

        verify(bookRepository).findByIsbn("9780261102394");

        verify(bookRepository).save(book);

        verify(loanRepository).save(any(Loan.class));
    }

    // Errors
    @Test
    void createLoanShouldThrowExceptionWhenStudentIsNotFound() {

        when(studentRepository.findByRegistrationNumber("123")).thenReturn(null);

        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class,
                () -> loanService.createLoan("123", "9780261102394"));

        assertEquals("Student not found.", exception.getMessage());

        verify(studentRepository).findByRegistrationNumber("123");
    }

    @Test
    void createLoanShouldThrowExceptionWhenBookIsNotFound() {

        Student student = new Student();
        student.setName("Carlos");
        student.setRegistrationNumber("123");

        when(studentRepository.findByRegistrationNumber("123")).thenReturn(student);

        when(bookRepository.findByIsbn("9780132350884")).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> loanService.createLoan("123", "9780132350884"));

        assertEquals("Book not found.", exception.getMessage());

        verify(bookRepository).findByIsbn("9780132350884");
    }

    @Test
    void createLoanShouldThrowExceptionWhenBookIsUnavailable() {

        Student student = new Student();
        student.setName("Carlos");
        student.setRegistrationNumber("123");

        Book book = new Book();
        book.setIsbn("9780132350884");
        book.setTitle("Clean Code");
        book.setAvailable(false);

        when(studentRepository.findByRegistrationNumber("123")).thenReturn(student);

        when(bookRepository.findByIsbn("9780132350884")).thenReturn(Optional.of(book));

        BookUnavailableException exception = assertThrows(BookUnavailableException.class,
                () -> loanService.createLoan("123", "9780132350884"));

        assertEquals("Book is unavailable.", exception.getMessage());

        verify(bookRepository).findByIsbn("9780132350884");
    }

    //==================================================
    // TESTS - RETURN LOAN
    //==================================================

    // Success
    @Test
    void returnLoanSuccessfully() {

        Loan loan = new Loan();
        Student student = new Student();
        Book book = new Book();

        student.setName("Carlos");
        student.setRegistrationNumber("1234");

        book.setTitle("The Lord of the Rings");
        book.setIsbn("9780261102394");
        book.setAvailable(false);

        loan.setStudent(student);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusMonths(1));
        loan.setStatus(LoanStatus.ACTIVE);

        when(loanRepository.findByStudentRegistrationNumberAndStatus("1234", LoanStatus.ACTIVE))
                .thenReturn(Optional.of(loan));

        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        LoanResponse result = loanService.returnLoan("1234");

        assertNotNull(result);

        assertEquals("Carlos", result.student().name());
        assertEquals("1234", result.student().registrationNumber());

        assertEquals("The Lord of the Rings", result.book().title());

        assertEquals("9780261102394", result.book().isbn());

        assertEquals("RETURNED", result.status());

        assertTrue(book.isAvailable());

        verify(loanRepository).findByStudentRegistrationNumberAndStatus("1234", LoanStatus.ACTIVE);

        verify(bookRepository).save(book);

        verify(loanRepository).save(loan);
    }

    // Error
    @Test
    void returnLoanShouldThrowExceptionWhenLoanIsNotFound() {

        when(loanRepository.findByStudentRegistrationNumberAndStatus("123", LoanStatus.ACTIVE))
                .thenReturn(Optional.empty());

        LoanNotFoundException exception = assertThrows(LoanNotFoundException.class,
                () -> loanService.returnLoan("123"));

        assertEquals("Loan not found.", exception.getMessage());

        verify(loanRepository).findByStudentRegistrationNumberAndStatus("123", LoanStatus.ACTIVE);
    }

    //==================================================
    // TESTS - LIST
    //==================================================

    // Success
    @Test
    void listLoansSuccessfully() {

        Student student1 = new Student();
        student1.setName("Carlos");
        student1.setRegistrationNumber("123456");

        Book book1 = new Book();
        book1.setTitle("The Lord of the Rings");
        book1.setIsbn("9780261102394");
        book1.setAvailable(false);

        Student student2 = new Student();
        student2.setName("Maria");
        student2.setRegistrationNumber("654321");

        Book book2 = new Book();
        book2.setTitle("Clean Code");
        book2.setIsbn("9780261102943");
        book2.setAvailable(false);

        Loan loan1 = new Loan();
        loan1.setStudent(student1);
        loan1.setBook(book1);
        loan1.setLoanDate(LocalDate.now());
        loan1.setDueDate(LocalDate.now().plusMonths(1));
        loan1.setStatus(LoanStatus.ACTIVE);

        Loan loan2 = new Loan();
        loan2.setStudent(student2);
        loan2.setBook(book2);
        loan2.setLoanDate(LocalDate.now());
        loan2.setDueDate(LocalDate.now().plusMonths(1));
        loan2.setStatus(LoanStatus.ACTIVE);

        List<Loan> loans = List.of(loan1, loan2);

        when(loanRepository.findAll()).thenReturn(loans);

        List<LoanResponse> result = loanService.list();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Carlos", result.get(0).student().name());

        assertEquals("123456", result.get(0).student().registrationNumber());

        assertEquals("The Lord of the Rings", result.get(0).book().title());

        assertEquals("9780261102394", result.get(0).book().isbn());

        assertEquals("Maria", result.get(1).student().name());

        assertEquals("654321", result.get(1).student().registrationNumber());

        assertEquals("Clean Code", result.get(1).book().title());

        assertEquals("9780261102943", result.get(1).book().isbn());

        verify(loanRepository).findAll();
    }

    //==================================================
    // TESTS - FIND BY STATUS
    //==================================================

    // Success
    @Test
    void findLoansByStatusSuccessfully() {

        Student student = new Student();
        student.setName("Carlos");
        student.setRegistrationNumber("123456");

        Book book = new Book();
        book.setTitle("The Lord of the Rings");
        book.setIsbn("9780261102394");
        book.setAvailable(false);

        Loan loan1 = new Loan();
        loan1.setStudent(student);
        loan1.setBook(book);
        loan1.setStatus(LoanStatus.RETURNED);
        loan1.setLoanDate(LocalDate.now());
        loan1.setDueDate(LocalDate.now().plusMonths(1));

        Loan loan2 = new Loan();
        loan2.setStudent(student);
        loan2.setBook(book);
        loan2.setStatus(LoanStatus.RETURNED);
        loan2.setLoanDate(LocalDate.now());
        loan2.setDueDate(LocalDate.now().plusMonths(1));

        List<Loan> loans = List.of(loan1, loan2);

        when(loanRepository.findByStatus(LoanStatus.RETURNED)).thenReturn(loans);

        List<LoanResponse> result = loanService.findByStatus(LoanStatus.RETURNED);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("RETURNED", result.get(0).status());

        assertEquals("RETURNED", result.get(1).status());

        verify(loanRepository).findByStatus(LoanStatus.RETURNED);
    }

    //==================================================
    // TESTS - FIND BY STUDENT REGISTRATION NUMBER
    //==================================================

    // Success
    @Test
    void findLoansByStudentRegistrationNumberSuccessfully() {

        Student student = new Student();
        student.setName("Carlos");
        student.setRegistrationNumber("123456");

        Book book = new Book();
        book.setTitle("The Lord of the Rings");
        book.setIsbn("9780261102394");
        book.setAvailable(false);

        Loan loan1 = new Loan();
        loan1.setStudent(student);
        loan1.setBook(book);
        loan1.setLoanDate(LocalDate.now());
        loan1.setDueDate(LocalDate.now().plusMonths(1));
        loan1.setStatus(LoanStatus.ACTIVE);

        Loan loan2 = new Loan();
        loan2.setStudent(student);
        loan2.setBook(book);
        loan2.setLoanDate(LocalDate.now());
        loan2.setDueDate(LocalDate.now().plusMonths(1));
        loan2.setStatus(LoanStatus.ACTIVE);

        List<Loan> loans = List.of(loan1, loan2);

        when(loanRepository.findByStudentRegistrationNumber("123456")).thenReturn(loans);

        List<LoanResponse> result = loanService.findByStudentRegistrationNumber("123456");

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Carlos", result.get(0).student().name());

        assertEquals("123456", result.get(0).student().registrationNumber());

        verify(loanRepository).findByStudentRegistrationNumber("123456");
    }

    //===========================================================
    // TESTS - CHECK WHETHER STUDENT HAS AN ACTIVE LOAN
    //===========================================================

    // Success
    @Test
    void hasActiveLoanReturnsTrue() {

        when(loanRepository.existsByStudentRegistrationNumberAndStatus("123", LoanStatus.ACTIVE)).thenReturn(true);

        boolean result = loanService.hasActiveLoan("123");

        assertTrue(result);

        verify(loanRepository).existsByStudentRegistrationNumberAndStatus("123", LoanStatus.ACTIVE);
    }

    @Test
    void hasActiveLoanReturnsFalse() {

        when(loanRepository.existsByStudentRegistrationNumberAndStatus("123", LoanStatus.ACTIVE)).thenReturn(false);

        boolean result = loanService.hasActiveLoan("123");

        assertFalse(result);

        verify(loanRepository).existsByStudentRegistrationNumberAndStatus("123", LoanStatus.ACTIVE);
    }
}