package br.com.carlos.Owl.exception.Livros;

public class LivroNaoEncontradoException extends RuntimeException {

    public LivroNaoEncontradoException() {
        super("Livro não encontrado.");
    }

}
