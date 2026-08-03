package br.com.carlos.Owl.exception.Livros;

public class LivroIndisponivelException extends RuntimeException {

    public LivroIndisponivelException() {
        super("Livro indisponível para empréstimo.");
    }

}
