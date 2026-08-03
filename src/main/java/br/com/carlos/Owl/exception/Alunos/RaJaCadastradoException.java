package br.com.carlos.Owl.exception.Alunos;

public class RaJaCadastradoException extends RuntimeException {

    public RaJaCadastradoException() {
        super("RA já cadastrado.");
    }

}
