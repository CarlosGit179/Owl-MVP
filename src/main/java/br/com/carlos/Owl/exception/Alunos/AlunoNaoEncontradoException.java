package br.com.carlos.Owl.exception.Alunos;

public class AlunoNaoEncontradoException extends RuntimeException {

    public AlunoNaoEncontradoException() {
        super("Aluno não encontrado.");
    }

}
