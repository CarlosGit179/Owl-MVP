package br.com.carlos.Owl.exception.Students;

public class RegistrationNumberAlreadyExistsException extends RuntimeException {

    public RegistrationNumberAlreadyExistsException() {
        super("Registration number already exists.");
    }

}
