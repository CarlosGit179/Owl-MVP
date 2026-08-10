package br.com.carlos.Owl.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.carlos.Owl.entity.Emprestimo;
import br.com.carlos.Owl.enums.StatusEmprestimo;
import br.com.carlos.Owl.service.EmprestimoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
@Tag(name = "Emprestimo", description = "API para gerenciamento de empréstimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @PostMapping
    @Operation(summary = "Realizar empréstimo", description = "Realiza um empréstimo de um livro para um aluno")
    public Emprestimo realizaEmprestimo(@RequestParam Long alunoId,
                                        @RequestParam Long livroId){
        return emprestimoService.realizarEmprestimo(alunoId, livroId);
                                        }

    @PutMapping("/{id}/devolver")
    @Operation(summary = "Devolver livro", description = "Registra a devolução de um livro emprestado")
    public Emprestimo devolver(@PathVariable Long id) {
        return emprestimoService.devolver(id);
    }

    @GetMapping
    @Operation(summary = "Listar todos os empréstimos", description = "Retorna uma lista de todos os empréstimos realizados")
    public List<Emprestimo> listar(){
        return emprestimoService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar empréstimo por ID", description = "Busca um empréstimo pelo seu ID")
    public Emprestimo buscarPorId(@PathVariable Long id){
        return emprestimoService.buscarPorId(id);
    }

    @GetMapping("/aluno/{alunoId}")
    @Operation(summary = "Buscar empréstimos por ID do aluno", description = "Busca empréstimos pelo ID do aluno")
    public List<Emprestimo> buscarPorAlunoId(@PathVariable Long alunoId){
        return emprestimoService.buscarPorAlunoId(alunoId);
    }

    @GetMapping("/livro/{livroId}")
    @Operation(summary = "Buscar empréstimos por ID do livro", description = "Busca empréstimos pelo ID do livro")
    public List<Emprestimo> buscarPorLivroId(@PathVariable Long livroId){
        return emprestimoService.buscarPorLivroId(livroId);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar empréstimos por status", description = "Busca empréstimos pelo status (EMPRESTADO, DEVOLVIDO, ATRASADO)")
    public List<Emprestimo> buscarPorStatus(@PathVariable StatusEmprestimo status) {
        return emprestimoService.buscarPorStatus(status);
    }
}
