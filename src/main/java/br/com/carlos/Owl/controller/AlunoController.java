package br.com.carlos.Owl.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.carlos.Owl.entity.Aluno;
import br.com.carlos.Owl.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controlador responsável por gerenciar as requisições relacionadas aos alunos.
 * <p>
 * Permite cadastrar, atualizar, excluir, listar e buscar alunos.
 * </p>
 */
@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
@Tag(name = "Aluno", description = "API para gerenciamento de alunos")
public class AlunoController {

    private final AlunoService alunoService;
    
    /**
     * Cadastra um novo aluno.
     *
     * @param aluno Aluno a ser cadastrado.
     * @return Aluno cadastrado.
     */
    @PostMapping
    @Operation(summary = "Cadastrar um novo aluno", description = "Cadastra um novo aluno na biblioteca")
    public Aluno cadastrar(@RequestBody Aluno aluno) {
        return alunoService.cadastrar(aluno);
    }

    /**
     * Atualiza um aluno existente.
     *
     * @param id ID do aluno a ser atualizado.
     * @param aluno Aluno com os dados atualizados.
     * @return Aluno atualizado.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar aluno", description = "Atualiza os dados de um aluno existente na biblioteca")
    public Aluno atualizar(@RequestBody Aluno aluno, @PathVariable Long id) {
        aluno.setId(id);
        return alunoService.atualizar(id, aluno);
    }

    /**
     * Exclui um aluno existente.
     *
     * @param id ID do aluno a ser excluído.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir aluno", description = "Exclui um aluno existente na biblioteca pelo seu ID")
    public void excluir(@PathVariable Long id) {
        alunoService.excluir(id);
    }

    /**
     * Lista todos os alunos.
     *
     * @return Lista de alunos.
     */
    @GetMapping
    @Operation(summary = "Listar todos os alunos", description = "Retorna uma lista de todos os alunos cadastrados na biblioteca")
    public List<Aluno> listar() {
        return alunoService.listar();
    }

    /**
     * Busca um aluno pelo seu ID.
     *
     * @param id ID do aluno.
     * @return Aluno encontrado.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar aluno por ID", description = "Busca um aluno na biblioteca pelo seu ID")
    public Aluno buscarPorId(@PathVariable Long id) {
        return alunoService.buscarPorId(id);
    }

    /**
     * Busca alunos pelo seu nome.
     *
     * @param nome Nome do aluno.
     * @return Lista de alunos encontrados.
     */
    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar alunos por nome", description = "Busca alunos na biblioteca pelo seu nome")
    public List<Aluno> buscarPorNome(@PathVariable String nome) {
        return alunoService.buscarPorNome(nome);
    }

    /**
     * Busca um aluno pelo seu RA.
     *
     * @param ra RA do aluno.
     * @return Aluno encontrado.
     */
    @GetMapping("/ra/{ra}")
    @Operation(summary = "Buscar aluno por RA", description = "Busca um aluno na biblioteca pelo seu RA")
    public Aluno buscarPorRa(@PathVariable String ra) {
        return alunoService.buscarPorRa(ra);
    }

}
