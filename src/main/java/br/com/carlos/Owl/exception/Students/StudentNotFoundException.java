package br.com.carlos.Owl.exception.Students;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException() {
        super("Student not found.");
    }

}
