package br.com.carlos.Owl.exception.Loans;

public class LoanAlreadyReturnedException extends RuntimeException {

    public LoanAlreadyReturnedException() {
        super("The loan has already been returned.");
    }

}
