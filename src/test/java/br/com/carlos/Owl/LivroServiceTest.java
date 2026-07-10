package br.com.carlos.Owl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.carlos.Owl.entity.Livro;
import br.com.carlos.Owl.repository.LivroRepository;
import br.com.carlos.Owl.service.LivroService;

@ExtendWith(MockitoExtension.class)
public class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private LivroService livroService;

    @Test
    void cadastrarLivroComSucesso() {
        Livro livro = new Livro();

        livro.setTitulo("Clean Code");
        livro.setAutor("Robert C. Martin");
        livro.setIsbn("9780132350884");
        livro.setEditora("Prentice Hall");
        livro.setAnoPublicacao(2008);
        livro.setCategoria("Programação");
        
        when (livroRepository.existsByIsbn(livro.getIsbn())).thenReturn(false);

        when (livroRepository.save(any(Livro.class))).thenReturn(livro);

        Livro resultado = livroService.cadastrar(livro);

        assertNotNull(resultado);
        assertEquals("Clean Code", resultado.getTitulo());
        assertTrue(resultado.isDisponivel());

        verify(livroRepository).save(any(Livro.class));
    }

    @Test
    void isbnJaCadastrado() {
        Livro livro = new Livro();
        livro.setIsbn("9780132350884");

        when(livroRepository.existsByIsbn(livro.getIsbn())).thenReturn(true);

       RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            livroService.cadastrar(livro);
        });

        assertEquals("ISBN já cadastrado", exception.getMessage());

        verify(livroRepository, never()).save(any(Livro.class));
    }

    @Test
    void listarTesteSucesso() {

        Livro livro1 = new Livro();
        livro1.setTitulo("Clean Code");
        Livro livro2 = new Livro();
        livro2.setTitulo("The Pragmatic Programmer");

        List <Livro> livros = List.of(livro1, livro2);

        when(livroRepository.findAll()).thenReturn(livros);

        List<Livro> resultado = livroService.listar();

        assertEquals(livros, resultado);
        assertEquals(2, resultado.size());
        assertEquals("Clean Code", resultado.get(0).getTitulo());
        assertEquals("The Pragmatic Programmer", resultado.get(1).getTitulo());

        verify(livroRepository).findAll();
    }

    @Test
    void buscarPorIdTesteSucesso() {
        Livro livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Clean Code");

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        Livro resultado = livroService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Clean Code", resultado.getTitulo());

        verify(livroRepository).findById(1L);
    }

    @Test
    void buscarPorIsbnTesteSucesso() {
        Livro livro = new Livro();
        livro.setIsbn("9780132350884");
        livro.setTitulo("Clean Code");

        when(livroRepository.findByIsbn("9780132350884")).thenReturn(Optional.of(livro));

        Livro resultado = livroService.buscarPorIsbn("9780132350884");

        assertEquals("9780132350884", resultado.getIsbn());
        assertEquals("Clean Code", resultado.getTitulo());

        verify(livroRepository).findByIsbn("9780132350884");
    }

    @Test
    void buscarPorTituloTesteSucesso() {

        Livro livro = new Livro();
        livro.setTitulo("Clean Code");

        List<Livro> livros = List.of(livro);

        when(livroRepository.findByTituloContainingIgnoreCase("Code")).thenReturn(livros);

        List<Livro> resultado = livroService.buscarPorTitulo("Code");

        assertEquals(livros, resultado);
        assertEquals(1, resultado.size());
        assertEquals("Clean Code", resultado.get(0).getTitulo());

        verify(livroRepository).findByTituloContainingIgnoreCase("Code");
    }

    @Test
    void atualizarLivroTesteSucesso() {
        Livro livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Clean Code");
        livro.setAutor("Carlos");
        livro.setIsbn("9780132350884");

        Livro livroAtualizado = new Livro();
        livroAtualizado.setTitulo("Clean Code");
        livroAtualizado.setAutor("Robert C. Martin");
        livroAtualizado.setIsbn("9780132350884");

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(livroRepository.save(any(Livro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Livro resultado = livroService.atualizar(1L, livroAtualizado);

        assertEquals("Clean Code", resultado.getTitulo());
        assertEquals("Robert C. Martin", resultado.getAutor());
        assertEquals("9780132350884", resultado.getIsbn());

        verify(livroRepository).findById(1L);
        verify(livroRepository).save(any(Livro.class));
    }

    @Test
    void excluirLivroTesteSucesso() {
        Livro livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Clean Code");

        when(livroRepository.existsById(1L)).thenReturn(true);

        livroService.excluir(1L);

        verify(livroRepository).existsById(1L);
        verify(livroRepository).deleteById(1L);

    }
}
