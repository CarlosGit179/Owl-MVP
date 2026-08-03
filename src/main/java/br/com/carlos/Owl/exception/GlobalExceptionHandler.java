package br.com.carlos.Owl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.carlos.Owl.exception.Alunos.AlunoComEmprestimoException;
import br.com.carlos.Owl.exception.Alunos.AlunoNaoEncontradoException;
import br.com.carlos.Owl.exception.Alunos.RaJaCadastradoException;
import br.com.carlos.Owl.exception.Emprestimos.EmprestimoDevolvidoException;
import br.com.carlos.Owl.exception.Emprestimos.EmprestimoEmAndamentoException;
import br.com.carlos.Owl.exception.Emprestimos.EmprestimoNaoEncontradoException;
import br.com.carlos.Owl.exception.Livros.IsbnJaCadastradoException;
import br.com.carlos.Owl.exception.Livros.LivroIndisponivelException;
import br.com.carlos.Owl.exception.Livros.LivroNaoEncontradoException;

/**
 * Centraliza o tratamento das exceções da aplicação,
 * retornando respostas HTTP adequadas para cada situação.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================
    // Alunos
    // =========================

    @ExceptionHandler(AlunoNaoEncontradoException.class)
    public ResponseEntity<String> tratarAlunoNaoEncontrado(AlunoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(RaJaCadastradoException.class)
    public ResponseEntity<String> tratarRaJaCadastrado(RaJaCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(AlunoComEmprestimoException.class)
    public ResponseEntity<String> tratarAlunoComEmprestimo(AlunoComEmprestimoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    // =========================
    // Livros
    // =========================

    @ExceptionHandler(LivroNaoEncontradoException.class)
    public ResponseEntity<String> tratarLivroNaoEncontrado(LivroNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(IsbnJaCadastradoException.class)
    public ResponseEntity<String> tratarIsbnJaCadastrado(IsbnJaCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(LivroIndisponivelException.class)
    public ResponseEntity<String> tratarLivroIndisponivel(LivroIndisponivelException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    // =========================
    // Empréstimos
    // =========================

    @ExceptionHandler(EmprestimoNaoEncontradoException.class)
    public ResponseEntity<String> tratarEmprestimoNaoEncontrado(EmprestimoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(EmprestimoEmAndamentoException.class)
    public ResponseEntity<String> tratarEmprestimoEmAndamento(EmprestimoEmAndamentoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(EmprestimoDevolvidoException.class)
    public ResponseEntity<String> tratarEmprestimoDevolvido(EmprestimoDevolvidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    // =========================
    // Genérico
    // =========================
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> tratarErroGenerico(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Ocorreu um erro interno no servidor.");
}
}