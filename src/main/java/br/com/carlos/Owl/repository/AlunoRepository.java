package br.com.carlos.Owl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.carlos.Owl.entity.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Aluno findByRa(String ra);
    Aluno findByNome(String nome);

    boolean existsById(long id);
    boolean existsByNome(String nome);
    boolean existsByRa(String ra);
    
    List<Aluno> findByNomeContainingIgnoreCase(String nome);
}
