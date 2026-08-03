package br.com.carlos.Owl.exception.Emprestimos;

public class EmprestimoDevolvidoException extends RuntimeException {

    public EmprestimoDevolvidoException() {
        super("Empréstimo já foi devolvido.");
    }

}
