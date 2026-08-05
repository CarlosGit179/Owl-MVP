package br.com.carlos.Owl.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.carlos.Owl.entity.Aluno;
import br.com.carlos.Owl.exception.Alunos.AlunoNaoEncontradoException;
import br.com.carlos.Owl.exception.Alunos.RaJaCadastradoException;
import br.com.carlos.Owl.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pelas regras de negócio relacionadas aos alunos.
 * <p>
 * Cadastro, listagem, busca, atualização e exclusão de alunos.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AlunoService {

    /**
     * Repositório de alunos.
     */
    private final AlunoRepository alunoRepository;

    /**
     * Cadastra um novo aluno.
     *
     * @param aluno Aluno a ser cadastrado.
     * @return Aluno cadastrado.
     * @throws RuntimeException Caso o nome ou RA do aluno já esteja cadastrado.
     */
    public Aluno cadastrar (Aluno aluno) {

        if (alunoRepository.existsByRa(aluno.getRa())) {
            throw new RaJaCadastradoException();
        }

        return alunoRepository.save(aluno);
    }

    /**
     * Lista todos os alunos.
     *
     * @return Lista de alunos.
     */
    public List<Aluno> listar() {
        return alunoRepository.findAll();
    }

    /**
     * Atualiza um aluno existente.
     *
     * @param id ID do aluno a ser atualizado.
     * @param alunoAtualizado Aluno com os dados atualizados.
     * @return Aluno atualizado.
     * @throws RuntimeException Caso o aluno não seja encontrado.
     */
    public Aluno atualizar (Long id, Aluno alunoAtualizado) {

        if (!alunoRepository.existsById(id)) {
            throw new AlunoNaoEncontradoException();
        }

        Aluno aluno = buscarPorId(id);

        aluno.setNome(alunoAtualizado.getNome());
        aluno.setRa(alunoAtualizado.getRa());

        return alunoRepository.save(aluno);
    }

    /**
     * Exclui um aluno existente.
     *
     * @param id ID do aluno a ser excluído.
     * @throws RuntimeException Caso o aluno não seja encontrado.
     */
    public void excluir(Long id) {

        if (!alunoRepository.existsById(id)) {
            throw new AlunoNaoEncontradoException();
        }

        alunoRepository.deleteById(id);
    }

    /**
     * Busca um aluno pelo seu ID.
     *
     * @param id ID do aluno.
     * @return Aluno encontrado.
     * @throws RuntimeException Caso o aluno não seja encontrado.
     */
    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new AlunoNaoEncontradoException());
    }

    /**
     * Busca um aluno pelo seu RA.
     *
     * @param ra RA do aluno.
     * @return Aluno encontrado.
     */
    public Aluno buscarPorRa(String ra) {
        return alunoRepository.findByRa(ra);
    }

    /**
     * Busca alunos pelo seu nome.
     *
     * @param nome Nome do aluno.
     * @return Lista de alunos encontrados.
     */
    public List<Aluno> buscarPorNome(String nome) {
        return alunoRepository.findByNomeContainingIgnoreCase(nome);
    }


}
