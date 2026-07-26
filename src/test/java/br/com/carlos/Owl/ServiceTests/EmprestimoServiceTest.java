package br.com.carlos.Owl.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.carlos.Owl.entity.Aluno;
import br.com.carlos.Owl.entity.Emprestimo;
import br.com.carlos.Owl.entity.Livro;
import br.com.carlos.Owl.enums.StatusEmprestimo;
import br.com.carlos.Owl.repository.AlunoRepository;
import br.com.carlos.Owl.repository.EmprestimoRepository;
import br.com.carlos.Owl.repository.LivroRepository;
import br.com.carlos.Owl.service.AlunoService;
import br.com.carlos.Owl.service.EmprestimoService;
import br.com.carlos.Owl.service.LivroService;

@ExtendWith(MockitoExtension.class)
public class EmprestimoServiceTest {

    @InjectMocks
    private EmprestimoService emprestimoService;
    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private LivroRepository livroRepository;
    @InjectMocks
    private LivroService livroService;

    @InjectMocks
    private AlunoService alunoService;
    @Mock
    private AlunoRepository alunoRepository;

    //Testes de Sucesso

    @Test
    void cadastrarEmprestimoComSucesso() {

        Livro livro = new Livro();
        Aluno aluno = new Aluno();

        aluno.setId(1L);
        aluno.setNome("Carlos");
        aluno.setRa("123456");

        livro.setId(1L);
        livro.setTitulo("O Senhor dos Anéis");
        livro.setIsbn("9780261102394");

    when(alunoRepository.findById(1L))
        .thenReturn(Optional.of(aluno));

    when(livroRepository.findById(1L))
            .thenReturn(Optional.of(livro));

    when(emprestimoRepository.existsByAlunoIdAndStatus(
            1L,
            StatusEmprestimo.EM_ANDAMENTO))
            .thenReturn(false);

    when(emprestimoRepository.save(any(Emprestimo.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    Emprestimo resultado =
            emprestimoService.realizarEmprestimo(1L, 1L);

    assertNotNull(resultado);

    assertEquals(StatusEmprestimo.EM_ANDAMENTO,
            resultado.getStatus());

    assertEquals(aluno,
            resultado.getAluno());

    assertEquals(livro,
            resultado.getLivro());

    assertFalse(livro.isDisponivel());

    verify(alunoRepository).findById(1L);

    verify(livroRepository).findById(1L);

    verify(livroRepository).save(livro);

    verify(emprestimoRepository)
            .save(any(Emprestimo.class));

    }


    @Test
    void devolverTestSucesso(){

        Emprestimo emprestimo = new Emprestimo();
        Aluno aluno = new Aluno();
        Livro livro = new Livro();

        aluno.setId(1L);
        aluno.setNome("Carlos");
        aluno.setRa("1234");

        livro.setId(1L);
        livro.setTitulo("Senhor dos Anéis");
        livro.setIsbn("9780261102394");
        livro.setDisponivel(false);

        emprestimo.setAluno(aluno);
        emprestimo.setLivro(livro);
        emprestimo.setStatus(StatusEmprestimo.EM_ANDAMENTO);

        when(emprestimoRepository.findById(1L))
                                        .thenReturn(Optional.of(emprestimo));

        when(emprestimoRepository.save(any(Emprestimo.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        when(livroRepository.save(any(Livro.class)))
                        .thenReturn(livro);

       Emprestimo resultado = emprestimoService.devolver(1L);     
       
       
       assertNotNull(resultado);
       assertEquals(StatusEmprestimo.DEVOLVIDO, resultado.getStatus());
       assertTrue(livro.isDisponivel());

       verify(emprestimoRepository).findById(1L);
       verify(livroRepository).save(livro);
       verify(emprestimoRepository).save(emprestimo);
                    
    }

    @Test
    void listarSucesso(){

        Aluno aluno1 = new Aluno();
        aluno1.setId(1L);
        aluno1.setNome("Carlos");
        aluno1.setRa("123456");
        Livro livro1 = new Livro();
        livro1.setTitulo("Senhor dos Anéis");
        livro1.setId(1L);
        livro1.setIsbn("9780261102394");
        livro1.setDisponivel(false);

        Aluno aluno2 = new Aluno();
        aluno2.setId(2L);
        aluno2.setNome("Maria");
        aluno2.setRa("654321");
        Livro livro2 = new Livro();
        livro2.setTitulo("Clean Code");
        livro2.setId(2L);
        livro2.setIsbn("9780261102943");
        livro2.setDisponivel(false);


        Emprestimo emprestimo1 = new Emprestimo();
        emprestimo1.setAluno(aluno1);
        emprestimo1.setLivro(livro1);
        Emprestimo emprestimo2 = new Emprestimo();
        emprestimo2.setAluno(aluno2);
        emprestimo2.setLivro(livro2);
    

        List<Emprestimo> emprestimos = List.of(emprestimo1, emprestimo2);

        when(emprestimoRepository.findAll())
                                 .thenReturn(emprestimos);

        List<Emprestimo> resultado = emprestimoService.listar();


        assertEquals(emprestimos, resultado);
        assertNotNull(resultado);

        verify(emprestimoRepository).findAll();
    }

    @Test
    void buscarPorIdSucesso(){

        Emprestimo emprestimo = new Emprestimo();

        emprestimo.setId(1L);

        when(emprestimoRepository.findById(emprestimo.getId())).thenReturn(Optional.of(emprestimo));

        Emprestimo resultado = emprestimoService.buscarPorId(emprestimo.getId());

        assertNotNull(resultado);
        assertEquals(emprestimo, resultado);

        verify(emprestimoRepository).findById(emprestimo.getId());
    }

    @Test
    void buscarAlunoPorIdSucesso(){

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Carlos");
        aluno.setRa("123456");

        Emprestimo emprestimo1 = new Emprestimo();
        emprestimo1.setAluno(aluno);
        Emprestimo emprestiimo2 = new Emprestimo();
        emprestiimo2.setAluno(aluno);

        List<Emprestimo> emprestimos = List.of(emprestimo1, emprestiimo2);

        when(emprestimoRepository.findByAlunoId(1L)).thenReturn(emprestimos);

        List<Emprestimo> resultado = emprestimoService.buscarPorAlunoId(1L);

        assertEquals(2, resultado.size());
        assertEquals(aluno, resultado.get(0).getAluno());
        assertEquals(aluno, resultado.get(1).getAluno());

        verify(emprestimoRepository).findByAlunoId(1L);

    }

    @Test
    void buscarLivroPorIdSucesso(){

        Livro livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Senhor dos Anéis");
        livro.setIsbn("9780261102394");

        Emprestimo emprestimo1 = new Emprestimo();
        emprestimo1.setLivro(livro);
        Emprestimo emprestiimo2 = new Emprestimo();
        emprestiimo2.setLivro(livro);

        List<Emprestimo> emprestimos = List.of(emprestimo1, emprestiimo2);

        when(emprestimoRepository.findByLivroId(1L)).thenReturn(emprestimos);

        List<Emprestimo> resultado = emprestimoService.buscarPorLivroId(1L);

        assertEquals(2, resultado.size());
        assertEquals(livro, resultado.get(0).getLivro());
        assertEquals(livro, resultado.get(1).getLivro());

        verify(emprestimoRepository).findByLivroId(1L);

    }

        @Test
    void buscarPorStatusSucesso(){

        Emprestimo emprestimo1 = new Emprestimo();
        emprestimo1.setStatus(StatusEmprestimo.DEVOLVIDO);
        Emprestimo emprestiimo2 = new Emprestimo();
        emprestiimo2.setStatus(StatusEmprestimo.DEVOLVIDO);

        List<Emprestimo> emprestimos = List.of(emprestimo1, emprestiimo2);

        when(emprestimoRepository.findByStatusEmprestmo(StatusEmprestimo.DEVOLVIDO)).thenReturn(emprestimos);

        List<Emprestimo> resultado = emprestimoService.buscarPorStatus(StatusEmprestimo.DEVOLVIDO);

        assertEquals(2, resultado.size());
        assertEquals(StatusEmprestimo.DEVOLVIDO, resultado.get(0).getStatus());
        assertEquals(StatusEmprestimo.DEVOLVIDO, resultado.get(1).getStatus());

        verify(emprestimoRepository).findByStatusEmprestmo(StatusEmprestimo.DEVOLVIDO);

    }
    

    // Testes Falha

    @Test
    void livroNaoDisponivelTeste(){

        Emprestimo emprestimo = new Emprestimo();
        Livro livro = new Livro();
        Aluno aluno = new Aluno();

        aluno.setId(1L);
        aluno.setNome("Carlos");
        aluno.setRa("123456");

        livro.setId(1L);
        livro.setTitulo("Senhor dos anéis");
        livro.setIsbn("9780132350884");
        livro.setDisponivel(false);

        emprestimo.setId(1L);
        emprestimo.setAluno(aluno);
        emprestimo.setLivro(livro);

        when(alunoRepository.findById(1L)).thenReturn((Optional.of(aluno)));
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        RuntimeException excecao = assertThrows(RuntimeException.class,
                                                    () -> emprestimoService.realizarEmprestimo(1L, 1L));

        
        assertEquals("Livro não disponível para empréstimo",
             excecao.getMessage());
    }

    @Test
    void alunoJaPosuiEmprestimoEmAndamentoTeste(){
        
        Emprestimo emprestimo = new Emprestimo();
        Livro livro = new Livro();
        Aluno aluno = new Aluno();

        aluno.setId(1L);
        aluno.setNome("Carlos");
        aluno.setRa("123456");

        livro.setId(1L);
        livro.setTitulo("Senhor dos anéis");
        livro.setIsbn("9780132350884");
        livro.setDisponivel(true);

        emprestimo.setId(1L);
        emprestimo.setAluno(aluno);
        emprestimo.setLivro(livro);
        emprestimo.setStatus(StatusEmprestimo.EM_ANDAMENTO);

        when(alunoRepository.findById(1L)).thenReturn((Optional.of(aluno)));
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(emprestimoRepository.existsByAlunoIdAndStatus(1L, StatusEmprestimo.EM_ANDAMENTO)).thenReturn(true);

        RuntimeException excecao = assertThrows(RuntimeException.class,
                                                    () -> emprestimoService.realizarEmprestimo(1L, 1L));


         assertEquals("Aluno já possui um empréstimo em andamento",
             excecao.getMessage());
    }

    @Test
    void livroJaDevolvidoTeste(){

        Emprestimo emprestimo = new Emprestimo();
        Aluno aluno = new Aluno();
        Livro livro = new Livro();

        aluno.setId(1L);
        aluno.setNome("Carlos");
        aluno.setRa("1234");

        livro.setId(1L);
        livro.setTitulo("Senhor dos Anéis");
        livro.setIsbn("9780261102394");
        livro.setDisponivel(false);

        emprestimo.setAluno(aluno);
        emprestimo.setLivro(livro);
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);

        when(emprestimoRepository.findById(1L))
                                        .thenReturn(Optional.of(emprestimo));

         RuntimeException excecao = assertThrows(RuntimeException.class,
                                                    () -> emprestimoService.devolver(1L));

        assertEquals("Empréstimo já foi devolvido",
             excecao.getMessage());
    }

}
