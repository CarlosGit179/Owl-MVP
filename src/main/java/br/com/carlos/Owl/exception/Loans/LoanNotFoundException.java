package br.com.carlos.Owl.exception.Loans;

public class LoanNotFoundException extends RuntimeException {

    public LoanNotFoundException() {
        super("Loan not found.");
    }

}
