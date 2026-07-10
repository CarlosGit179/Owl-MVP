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
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;
    
    @PostMapping
    public Aluno cadastrar(@RequestBody Aluno aluno) {
        return alunoService.cadastrar(aluno);
    }

    @PutMapping("/{id}")
    public Aluno atualizar(@RequestBody Aluno aluno, @PathVariable Long id) {
        aluno.setId(id);
        return alunoService.atualizar(id, aluno);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        alunoService.excluir(id);
    }

    @GetMapping
    public List<Aluno> listar() {
        return alunoService.listar();
    }

    @GetMapping("/{id}")
    public Aluno buscarPorId(@PathVariable Long id) {
        return alunoService.buscarPorId(id);
    }

    @GetMapping("/nome/{nome}")
    public List<Aluno> buscarPorNome(@PathVariable String nome) {
        return alunoService.buscarPorNome(nome);
    }

    @GetMapping("/ra/{ra}")
    public Aluno buscarPorRa(@PathVariable String ra) {
        return alunoService.buscarPorRa(ra);
    }

}
