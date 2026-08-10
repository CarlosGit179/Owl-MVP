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

import br.com.carlos.Owl.entity.Livro;
import br.com.carlos.Owl.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
@Tag(name = "Livro", description = "API para gerenciamento de livros")
public class LivroController {

    private final LivroService livroService;

    @PostMapping
    @Operation(summary = "Cadastrar um novo livro", description = "Cadastra um novo livro na biblioteca")
    public Livro cadastrar(@RequestBody Livro livro) {
        return livroService.cadastrar(livro);
    }

    @GetMapping
    @Operation(summary = "Listar todos os livros", description = "Retorna uma lista de todos os livros cadastrados na biblioteca")
    public List<Livro> listar() {
        return livroService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID", description = "Busca um livro na biblioteca pelo seu ID")
    public Livro buscarPorId(@PathVariable Long id) {
        return livroService.buscarPorId(id);
    }

    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "Buscar livro por ISBN", description = "Busca um livro na biblioteca pelo seu ISBN")
    public Livro buscarPorIsbn(@PathVariable String isbn) {
        return livroService.buscarPorIsbn(isbn);
    }

    @GetMapping("/titulo/{titulo}")
    @Operation(summary = "Buscar livros por título", description = "Busca livros na biblioteca pelo seu título")
    public List<Livro> buscarPorTitulo(@PathVariable String titulo) {
        return livroService.buscarPorTitulo(titulo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro", description = "Atualiza os dados de um livro existente na biblioteca")
    public Livro atualizar(@PathVariable Long id, @RequestBody Livro livro) {
        return livroService.atualizar(id, livro);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir livro", description = "Exclui um livro existente na biblioteca pelo seu ID")
    public void excluir(@PathVariable Long id) {
        livroService.excluir(id);
    }

}
