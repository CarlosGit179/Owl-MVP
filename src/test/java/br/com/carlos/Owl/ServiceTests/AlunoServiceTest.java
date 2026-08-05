package br.com.carlos.Owl.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import br.com.carlos.Owl.exception.Alunos.RaJaCadastradoException;
import br.com.carlos.Owl.repository.AlunoRepository;
import br.com.carlos.Owl.service.AlunoService;

@ExtendWith(MockitoExtension.class)
public class AlunoServiceTest {

        @InjectMocks
        private AlunoService alunoService;
        @Mock
        private AlunoRepository alunoRepository;

        //=========================
        // Testes de sucesso
        //=========================

        @Test
        void cadastrarAlunoComSucesso() {
                Aluno aluno = new Aluno();

                aluno.setNome("João da Silva");
                aluno.setRa("123456");

                when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

                Aluno resultado = alunoService.cadastrar(aluno);

                assertEquals(aluno, resultado);
                assertNotNull(resultado);
                
                verify(alunoRepository).save(any(Aluno.class));

        }

        @Test
        void listarAlunosComSucesso() {

            Aluno aluno1 = new Aluno();
            aluno1.setNome("João da Silva");
            aluno1.setRa("123456");

            Aluno aluno2 = new Aluno();
            aluno2.setNome("Maria Souza");
            aluno2.setRa("654321");

            List<Aluno> alunos = List.of(aluno1, aluno2);

            when(alunoRepository.findAll()).thenReturn(alunos);

            List<Aluno> resultado = alunoService.listar();

            assertEquals(alunos, resultado);
            assertNotNull(resultado);

            verify(alunoRepository).findAll();
        }

        @Test
        void atualizarAlunoComSucesso() {
            Aluno aluno = new Aluno();
            aluno.setId(1L);
            aluno.setNome("João Silva");
            aluno.setRa("123456");

            Aluno alunoAtualizado = new Aluno();
            alunoAtualizado.setNome("João da Silva");
            alunoAtualizado.setRa("654321");

            when(alunoRepository.findById(aluno.getId())).thenReturn(Optional.of(aluno));
            when(alunoRepository.save(any(Aluno.class))).thenReturn(alunoAtualizado);

            Aluno resultado = alunoService.atualizar(aluno.getId(), alunoAtualizado);


            assertEquals("João da Silva", resultado.getNome());
            assertEquals("654321", resultado.getRa());
            assertNotNull(resultado);

            verify(alunoRepository).save(any(Aluno.class));
            verify(alunoRepository).findById(aluno.getId());
        }

        @Test
        void excluirAlunoComSucesso() {
            Aluno aluno = new Aluno();
            aluno.setId(1L);
            aluno.setNome("João da Silva");
            aluno.setRa("123456");

            when(alunoRepository.existsById(aluno.getId())).thenReturn(true);

            alunoService.excluir(aluno.getId());

            verify(alunoRepository).existsById(aluno.getId());
            verify(alunoRepository).deleteById(aluno.getId());
        }

        @Test
        void buscarAlunoPorIdComSucesso() {
            Aluno aluno = new Aluno();
            aluno.setId(1L);
            aluno.setNome("João da Silva");
            aluno.setRa("123456");

            when(alunoRepository.findById(aluno.getId())).thenReturn(Optional.of(aluno));

            Aluno resultado = alunoService.buscarPorId(aluno.getId());

            assertEquals(aluno, resultado);
            assertNotNull(resultado);

            verify(alunoRepository).findById(aluno.getId());
        }

        @Test
        void buscarAlunoPorRaComSucesso() {
            Aluno aluno = new Aluno();
            aluno.setId(1L);
            aluno.setNome("João da Silva");
            aluno.setRa("123456");

            when(alunoRepository.findByRa(aluno.getRa())).thenReturn(aluno);

            Aluno resultado = alunoService.buscarPorRa(aluno.getRa());

            assertEquals(aluno, resultado);
            assertNotNull(resultado);

            verify(alunoRepository).findByRa(aluno.getRa());
        }

        @Test
        void buscarAlunoPorNomeComSucesso() {
            Aluno aluno1 = new Aluno();
            aluno1.setId(1L);
            aluno1.setNome("João da Silva");
            aluno1.setRa("123456");

            Aluno aluno2 = new Aluno();
            aluno2.setId(2L);
            aluno2.setNome("Maria da Silva");
            aluno2.setRa("654321");

            List<Aluno> alunos = List.of(aluno1, aluno2);

            when(alunoRepository.findByNomeContainingIgnoreCase("maria")).thenReturn(alunos);

            List<Aluno> resultado = alunoService.buscarPorNome("maria");

            assertEquals(alunos, resultado);
            assertNotNull(resultado);

            verify(alunoRepository).findByNomeContainingIgnoreCase("maria");
        }

        //=========================
        // Testes de exceção
        //=========================

        @Test
        void cadastrarAlunoNaoEncontrado(){

            Aluno aluno = new Aluno();

            aluno.setId(1L);
            aluno.setNome("Carlos");
            aluno.setRa("123456");

            when(alunoRepository.existsByRa("123456")).thenReturn(true);

            RaJaCadastradoException excecao = assertThrows(RaJaCadastradoException.class,
                                                    () -> alunoService.cadastrar(aluno));

            assertEquals("RA já cadastrado.",
             excecao.getMessage());

        }

}