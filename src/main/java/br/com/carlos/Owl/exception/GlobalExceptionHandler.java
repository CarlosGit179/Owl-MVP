package br.com.carlos.Owl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.carlos.Owl.exception.Books.BookHasActiveLoanException;
import br.com.carlos.Owl.exception.Books.BookNotFoundException;
import br.com.carlos.Owl.exception.Books.BookUnavailableException;
import br.com.carlos.Owl.exception.Books.IsbnAlreadyRegisteredException;
import br.com.carlos.Owl.exception.Loans.LoanAlreadyActiveException;
import br.com.carlos.Owl.exception.Loans.LoanAlreadyReturnedException;
import br.com.carlos.Owl.exception.Loans.LoanNotFoundException;
import br.com.carlos.Owl.exception.Students.RegistrationNumberAlreadyExistsException;
import br.com.carlos.Owl.exception.Students.StudentHasActiveLoanException;
import br.com.carlos.Owl.exception.Students.StudentNotFoundException;

/**
 * Centralizes application exception handling and returns appropriate HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================
    // Students
    // =========================

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<String> handleStudentNotFound(StudentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(RegistrationNumberAlreadyExistsException.class)
    public ResponseEntity<String> handleRegisterNumberAlreadyExists(RegistrationNumberAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(StudentHasActiveLoanException.class)
    public ResponseEntity<String> handleStudentHasActiveLoan(StudentHasActiveLoanException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // =========================
    // Books
    // =========================

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> handleBookNotFound(BookNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IsbnAlreadyRegisteredException.class)
    public ResponseEntity<String> handleIsbnAlreadyregistered(IsbnAlreadyRegisteredException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(BookUnavailableException.class)
    public ResponseEntity<String> handleBookUnvailable(BookUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(BookHasActiveLoanException.class)
    public ResponseEntity<String> handleBookHasActiveLoan(BookHasActiveLoanException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // =========================
    // Loans
    // =========================

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<String> handleLoanNotFound(LoanNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(LoanAlreadyActiveException.class)
    public ResponseEntity<String> handleLoanAlreadyActive(LoanAlreadyActiveException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(LoanAlreadyReturnedException.class)
    public ResponseEntity<String> handleLoanAlredyReturned(LoanAlreadyReturnedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // =========================
    // Generic
    // =========================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred.");
    }
}
