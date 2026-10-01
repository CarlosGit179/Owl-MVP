package br.com.carlos.Owl.exception.Books;

public class BookHasActiveLoanException extends RuntimeException {

    public BookHasActiveLoanException() {
        super("The book has an active loan.");
    }

}
