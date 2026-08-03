package br.com.carlos.Owl.exception.Alunos;

public class AlunoComEmprestimoException extends RuntimeException {

    public AlunoComEmprestimoException() {
        super("Aluno possui empréstimo em andamento.");
    }

}
