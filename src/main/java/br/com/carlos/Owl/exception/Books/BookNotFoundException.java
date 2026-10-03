package br.com.carlos.Owl.exception.Books;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException() {
        super("Book not found.");
    }

}
