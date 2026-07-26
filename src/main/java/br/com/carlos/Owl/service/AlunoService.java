package br.com.carlos.Owl.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.carlos.Owl.entity.Aluno;
import br.com.carlos.Owl.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public Aluno cadastrar (Aluno aluno) {
        if (alunoRepository.existsByNome(aluno.getNome())) {
            throw new RuntimeException("Nome já cadastrado");
        }

        if (alunoRepository.existsByRa(aluno.getRa())) {
            throw new RuntimeException("RA já cadastrado");
        }

        return alunoRepository.save(aluno);
    }

    public List<Aluno> listar() {
        return alunoRepository.findAll();
    }

    public Aluno atualizar (Long id, Aluno alunoAtualizado) {

        if (!alunoRepository.existsById(id)) {
            throw new RuntimeException("Aluno não encontrado");
        }

        Aluno aluno = buscarPorId(id);

        aluno.setNome(alunoAtualizado.getNome());
        aluno.setRa(alunoAtualizado.getRa());

        return alunoRepository.save(aluno);
    }

    public void excluir(Long id) {

        if (!alunoRepository.existsById(id)) {
            throw new RuntimeException("Aluno não encontrado");
        }

        alunoRepository.deleteById(id);
    }

    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
    }

    public Aluno buscarPorRa(String ra) {
        return alunoRepository.findByRa(ra);
    }

    public List<Aluno> buscarPorNome(String nome) {
        return alunoRepository.findByNomeContainingIgnoreCase(nome);
    }


}
