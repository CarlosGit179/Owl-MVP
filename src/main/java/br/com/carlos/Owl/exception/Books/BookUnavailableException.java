package br.com.carlos.Owl.exception.Books;

public class BookUnavailableException extends RuntimeException {

    public BookUnavailableException() {
        super("Book is unavailable.");
    }

}
