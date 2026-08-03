package br.com.carlos.Owl.exception.Emprestimos;

public class EmprestimoEmAndamentoException extends RuntimeException {

    public EmprestimoEmAndamentoException() {
        super("O aluno já possui um empréstimo em andamento.");
    }
}
