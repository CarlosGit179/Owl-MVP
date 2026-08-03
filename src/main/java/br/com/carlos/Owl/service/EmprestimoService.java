package br.com.carlos.Owl.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.carlos.Owl.entity.Aluno;
import br.com.carlos.Owl.entity.Emprestimo;
import br.com.carlos.Owl.entity.Livro;
import br.com.carlos.Owl.enums.StatusEmprestimo;
import br.com.carlos.Owl.repository.AlunoRepository;
import br.com.carlos.Owl.repository.EmprestimoRepository;
import br.com.carlos.Owl.repository.LivroRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pelas regras de negócio relacionadas aos empréstimos.
 * <p>
 * Realização de empréstimos, devoluções, listagem e busca de empréstimos.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EmprestimoService{

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final AlunoRepository alunoRepository;

    /**
     * Realiza um empréstimo de um livro para um aluno.
     *
     * @param alunoId ID do aluno que realizará o empréstimo.
     * @param livroId ID do livro a ser emprestado.
     * @return Empréstimo realizado.
     * @throws RuntimeException Caso o aluno ou livro não sejam encontrados, caso o livro não esteja disponível ou caso o aluno já possua um empréstimo em andamento.
     */
    @Transactional
    public Emprestimo realizarEmprestimo(Long alunoId, Long livroId) {

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));


        if (!livro.isDisponivel()) {
            throw new RuntimeException("Livro não disponível para empréstimo");
        }
        if (emprestimoRepository.existsByAlunoIdAndStatus(alunoId, StatusEmprestimo.EM_ANDAMENTO)) {
            throw new RuntimeException("Aluno já possui um empréstimo em andamento");
        }


       Emprestimo emprestimo = Emprestimo.builder()
                .aluno(aluno)
                .livro(livro)
                .dataEmprestimo(LocalDate.now())
                .dataPrevistaDevolucao(LocalDate.now().plusMonths(1))
                .status(StatusEmprestimo.EM_ANDAMENTO)
                .build();

        livro.setDisponivel(false);
        livroRepository.save(livro);
        return emprestimoRepository.save(emprestimo);
    }


    /**
     * Realiza a devolução de um empréstimo.
     *
     * @param emprestimoId ID do empréstimo a ser devolvido.
     * @return Empréstimo devolvido.
     * @throws RuntimeException Caso o empréstimo não seja encontrado ou caso o empréstimo já tenha sido devolvido.
     */
    @Transactional
    public Emprestimo devolver(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        if (emprestimo.getStatus() == StatusEmprestimo.DEVOLVIDO) {
            throw new RuntimeException("Empréstimo já foi devolvido");
        }

        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);

        Livro livro = emprestimo.getLivro();
        livro.setDisponivel(true);
        livroRepository.save(livro);

        return emprestimoRepository.save(emprestimo);
    }


    /**
     * Lista todos os empréstimos.
     *
     * @return Lista de empréstimos.
     */
    public List<Emprestimo> listar() {
        return emprestimoRepository.findAll();
    }
    /**
     * Busca um empréstimo pelo seu ID.
     *
     * @param id ID do empréstimo.
     * @return Empréstimo encontrado.
     * @throws RuntimeException Caso o empréstimo não seja encontrado.
     */
    public Emprestimo buscarPorId(Long id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));
    }
    /**
     * Busca empréstimos pelo ID do aluno.
     *
     * @param alunoId ID do aluno.
     * @return Lista de empréstimos encontrados.
     */
    public List<Emprestimo> buscarPorAlunoId(Long alunoId) {
        return emprestimoRepository.findByAlunoId(alunoId);
    }
    /**
     * Busca empréstimos pelo ID do livro.
     *
     * @param livroId ID do livro.
     * @return Lista de empréstimos encontrados.
     */
    public List<Emprestimo> buscarPorLivroId(Long livroId) {
        return emprestimoRepository.findByLivroId(livroId);
    }
    /**
     * Busca empréstimos pelo status.
     *
     * @param status Status do empréstimo.
     * @return Lista de empréstimos encontrados.
     */
    public List<Emprestimo> buscarPorStatus(StatusEmprestimo status) {
        return emprestimoRepository.findByStatus(status);
    }


}
