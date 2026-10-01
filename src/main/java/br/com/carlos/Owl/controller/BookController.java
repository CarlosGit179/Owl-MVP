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

import br.com.carlos.Owl.dto.infra.request.BookRequest;
import br.com.carlos.Owl.dto.infra.response.BookResponse;
import br.com.carlos.Owl.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Book", description = "API for managing books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    @Operation(summary = "Register a new book", description = "Registers a new book in the library")
    public BookResponse register(@RequestBody BookRequest request) {
        return bookService.register(request);
    }

    @PutMapping("/{isbn}")
    @Operation(summary = "Update book", description = "Updates the data of an existing book in the library")
    public BookResponse update(@PathVariable String isbn, @RequestBody BookRequest request) {
        return bookService.update(isbn, request);
    }

    @DeleteMapping("/{isbn}")
    @Operation(summary = "Delete book", description = "Deletes an existing book from the library by ISBN")
    public void delete(@PathVariable String isbn) {
        bookService.delete(isbn);
    }

    @GetMapping
    @Operation(summary = "List all books", description = "Returns a list of all books registered in the library")
    public List<BookResponse> list() {
        return bookService.list();
    }

    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "Find book by ISBN", description = "Finds a book in the library by ISBN")
    public BookResponse findByIsbn(@PathVariable String isbn) {
        return bookService.findByIsbn(isbn);
    }

    @GetMapping("/title/{title}")
    @Operation(summary = "Find books by title", description = "Finds books in the library by title")
    public List<BookResponse> findByTitle(@PathVariable String title) {
        return bookService.findByTitle(title);
    }

}
