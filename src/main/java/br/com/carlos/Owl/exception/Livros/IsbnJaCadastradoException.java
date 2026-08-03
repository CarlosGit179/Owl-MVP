package br.com.carlos.Owl.exception.Livros;

public class IsbnJaCadastradoException extends RuntimeException {

    public IsbnJaCadastradoException() {
        super("ISBN já cadastrado.");
    }

}
