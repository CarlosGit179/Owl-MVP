package br.com.carlos.Owl.exception.Books;

public class IsbnAlreadyRegisteredException extends RuntimeException {

    public IsbnAlreadyRegisteredException() {
        super("The isbn is already registered.");
    }

}
