package br.com.carlos.Owl.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.carlos.Owl.entity.Livro;
import br.com.carlos.Owl.repository.LivroRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;

    public Livro cadastrar(Livro livro) {
        if (livroRepository.existsByIsbn(livro.getIsbn())) {
            throw new RuntimeException("ISBN já cadastrado");
        }

        livro.setDisponivel(true);

        return livroRepository.save(livro);
    }

    public List<Livro> listar() {
        return livroRepository.findAll();
    }

    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
    }

    public Livro buscarPorIsbn(String isbn) {
        return livroRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

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

    public void excluir(Long id) {

        if (!livroRepository.existsById(id)) {
            throw new RuntimeException("Livro não encontrado.");
        }

        livroRepository.deleteById(id);
    }
}

