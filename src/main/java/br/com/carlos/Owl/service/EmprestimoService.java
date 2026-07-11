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

@Service
@RequiredArgsConstructor
@Transactional
public class EmprestimoService{

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final AlunoRepository alunoRepository;

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
        return emprestimoRepository.save(emprestimo);
    }


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


    public List<Emprestimo> listar() {
        return emprestimoRepository.findAll();
    }
    public Emprestimo buscarPorId(Long id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));
    }
    public List<Emprestimo> buscarPorAlunoId(Long alunoId) {
        return emprestimoRepository.findByAlunoId(alunoId);
    }
    public List<Emprestimo> buscarPorLivroId(Long livroId) {
        return emprestimoRepository.findByLivroId(livroId);
    }
    public List<Emprestimo> buscarPorStatus(StatusEmprestimo status) {
        return emprestimoRepository.findByStatusEmprestmo(status);
    }


}
