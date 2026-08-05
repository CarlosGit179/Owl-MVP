package br.com.carlos.Owl.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.carlos.Owl.entity.Livro;
import br.com.carlos.Owl.exception.Livros.IsbnJaCadastradoException;
import br.com.carlos.Owl.exception.Livros.LivroNaoEncontradoException;
import br.com.carlos.Owl.repository.LivroRepository;
import lombok.RequiredArgsConstructor;

/**
    *Serviço responsável pelas regras de negócio relacionadas aos livros.
    *<p>
    *Cadastro, listagem, busca, atualização e exclusão de livros.
    *</p>
    */
@Service
@RequiredArgsConstructor
public class LivroService {

    /**
     * Repositório de livros.
     */
    private final LivroRepository livroRepository;

    /**
     * Cadastra um novo livro.
     *
     * @param livro Livro a ser cadastrado.
     * @return Livro cadastrado.
     * @throws IsbnJaCadastradoException Caso o ISBN do livro já esteja cadastrado.
     */
    public Livro cadastrar(Livro livro) {
        if (livroRepository.existsByIsbn(livro.getIsbn())) {
            throw new IsbnJaCadastradoException();
        }

        livro.setDisponivel(true);

        return livroRepository.save(livro);
    }

    /**
     * Lista todos os livros.
     *
     * @return Lista de livros.
     */
    public List<Livro> listar() {
        return livroRepository.findAll();
    }

    /**
     * Busca um livro pelo seu ID.
     *
     * @param id ID do livro.
     * @return Livro encontrado.
     * @throws RuntimeException Caso o livro não seja encontrado.
     */
    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new LivroNaoEncontradoException());
    }

    /**
     * Busca um livro pelo seu ISBN.
     *
     * @param isbn ISBN do livro.
     * @return Livro encontrado.
     * @throws RuntimeException Caso o livro não seja encontrado.
     */
    public Livro buscarPorIsbn(String isbn) {
        return livroRepository.findByIsbn(isbn)
                .orElseThrow(() -> new LivroNaoEncontradoException());
    }

    /**
     * Busca livros pelo seu título.
     *
     * @param titulo Título do livro.
     * @return Lista de livros encontrados.
     */
    public List<Livro> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    /**
     * Atualiza um livro existente.
     *
     * @param id ID do livro a ser atualizado.
     * @param livroAtualizado Livro com os dados atualizados.
     * @return Livro atualizado.
     * @throws RuntimeException Caso o livro não seja encontrado.
     */
    public Livro atualizar (Long id, Livro livroAtualizado) {
        Livro livro = buscarPorId(id);

        livro.setTitulo(livroAtualizado.getTitulo());
        livro.setAutor(livroAtualizado.getAutor());
        livro.setIsbn(livroAtualizado.getIsbn());
        livro.setEditora(livroAtualizado.getEditora());
        livro.setAnoPublicacao(livroAtualizado.getAnoPublicacao());
        livro.setCategoria(livroAtualizado.getCategoria());

        return livroRepository.save(livro);
    }

    /**
     * Exclui um livro existente.
     *
     * @param id ID do livro a ser excluído.
     * @throws RuntimeException Caso o livro não seja encontrado.
     */
    public void excluir(Long id) {

        if (!livroRepository.existsById(id)) {
            throw new LivroNaoEncontradoException();
        }

        livroRepository.deleteById(id);
    }
}