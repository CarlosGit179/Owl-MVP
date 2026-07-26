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
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @PostMapping
    public Emprestimo realizaEmprestimo(@RequestParam Long AlunoId,
                                        @RequestParam Long LivroId){
        return emprestimoService.realizarEmprestimo(AlunoId, LivroId);
                                        }

    @PutMapping("/{id}/devolver")
    public Emprestimo devolver(@PathVariable Long id) {
        return emprestimoService.devolver(id);
    }

    @GetMapping
    public List<Emprestimo> listar(){
        return emprestimoService.listar();
    }

    @GetMapping("/{id}")
    public Emprestimo buscarPorId(@PathVariable Long Id){
        return emprestimoService.buscarPorId(Id);
    }

    @GetMapping("/aluno/{alunoId}")
    public List<Emprestimo> buscarPorAlunoId(@PathVariable Long AlunoId){
        return emprestimoService.buscarPorAlunoId(AlunoId);
    }

    @GetMapping("/livro/{livroId}")
    public List<Emprestimo> buscarPorLivroId(@PathVariable Long livroId){
        return emprestimoService.buscarPorLivroId(livroId);
    }

    @GetMapping("/status/{status}")
    public List<Emprestimo> buscarPorStatus(@PathVariable StatusEmprestimo status) {
        return emprestimoService.buscarPorStatus(status);
    }
}
