package br.com.carlos.Owl.exception.Emprestimos;

public class EmprestimoNaoEncontradoException extends RuntimeException {

    public EmprestimoNaoEncontradoException() {
        super("Empréstimo não encontrado.");
    }

}
