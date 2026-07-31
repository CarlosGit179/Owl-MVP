package br.com.carlos.Owl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.carlos.Owl.entity.Emprestimo;
import br.com.carlos.Owl.enums.StatusEmprestimo;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByAlunoId(Long alunoId);

    List<Emprestimo> findByLivroId(Long livroId);

    List<Emprestimo> findByStatus(StatusEmprestimo status);

    boolean existsByAlunoIdAndStatus(Long alunoId, StatusEmprestimo status);
}
