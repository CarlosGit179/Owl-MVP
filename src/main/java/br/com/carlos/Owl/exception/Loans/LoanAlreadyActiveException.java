package br.com.carlos.Owl.exception.Loans;

public class LoanAlreadyActiveException extends RuntimeException {

    public LoanAlreadyActiveException() {
        super("The loan is already active.");
    }
}
