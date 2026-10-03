package br.com.carlos.Owl.exception.Students;

public class StudentHasActiveLoanException extends RuntimeException {

    public StudentHasActiveLoanException() {
        super("The student already has an active Loan.");
    }

}
