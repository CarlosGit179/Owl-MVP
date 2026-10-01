package br.com.carlos.Owl.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.carlos.Owl.dto.infra.request.BookRequest;
import br.com.carlos.Owl.dto.infra.response.BookResponse;
import br.com.carlos.Owl.entity.Book;
import br.com.carlos.Owl.exception.Books.BookNotFoundException;
import br.com.carlos.Owl.exception.Books.IsbnAlreadyRegisteredException;
import br.com.carlos.Owl.repository.BookRepository;
import br.com.carlos.Owl.service.BookService;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    //==================================================
    // TESTS - REGISTER
    //==================================================

    // Success
    @Test
    void registerBookSuccessfully() {

        BookRequest request = new BookRequest("Clean Code", "Robert C. Martin", "9780132350884", 2008, "Programming",
                "Prentice Hall");

        when(bookRepository.existsByIsbn(request.isbn())).thenReturn(false);

        Book savedBook = new Book();
        savedBook.setTitle("Clean Code");
        savedBook.setAuthor("Robert C. Martin");
        savedBook.setIsbn("9780132350884");
        savedBook.setPublisher("Prentice Hall");
        savedBook.setPublicationYear(2008);
        savedBook.setCategory("Programming");
        savedBook.setAvailable(true);

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookResponse result = bookService.register(request);

        assertNotNull(result);
        assertEquals("Clean Code", result.title());
        assertEquals("Robert C. Martin", result.author());
        assertEquals("Programming", result.category());
        assertTrue(result.available());

        verify(bookRepository).save(any(Book.class));
    }

    // Errors
    @Test
    void registerShouldThrowExceptionWhenIsbnAlreadyExists() {

        BookRequest request = new BookRequest("Clean Code", "Robert C. Martin", "9780132350884", 2008, "Programming",
                "Prentice Hall");

        when(bookRepository.existsByIsbn(request.isbn())).thenReturn(true);

        IsbnAlreadyRegisteredException exception = assertThrows(IsbnAlreadyRegisteredException.class,
                () -> bookService.register(request));

        assertEquals("The isbn is already registered.", exception.getMessage());

        verify(bookRepository, never()).save(any(Book.class));
    }

    //==================================================
    // TESTS - LIST
    //==================================================

    // Success
    @Test
    void listBooksSuccessfully() {

        Book book1 = new Book();
        book1.setTitle("Clean Code");

        Book book2 = new Book();
        book2.setTitle("The Pragmatic Programmer");

        List<Book> books = List.of(book1, book2);

        when(bookRepository.findAll()).thenReturn(books);

        List<BookResponse> result = bookService.list();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Clean Code", result.get(0).title());
        assertEquals("The Pragmatic Programmer", result.get(1).title());
        assertTrue(result.get(0).available());

        verify(bookRepository).findAll();
    }

    //==================================================
    // TESTS - UPDATE
    //==================================================

    // Success
    @Test
    void updateBookSuccessfully() {

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Carlos");
        book.setIsbn("9780132350884");

        BookRequest request = new BookRequest("Clean Code", "Robert C. Martin", "9780132350884", 2008, "Programming",
                "Prentice Hall");

        when(bookRepository.findByIsbn("9780132350884")).thenReturn(Optional.of(book));

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookResponse result = bookService.update("9780132350884", request);

        assertNotNull(result);
        assertEquals("Clean Code", result.title());
        assertEquals("Robert C. Martin", result.author());
        assertEquals("9780132350884", result.isbn());

        verify(bookRepository).findByIsbn("9780132350884");

        verify(bookRepository).save(any(Book.class));
    }

    //==================================================
    // TESTS - DELETE
    //==================================================

    // Success
    @Test
    void deleteBookSuccessfully() {

        Book book = new Book();
        book.setIsbn("9780132350884");
        book.setTitle("Clean Code");
        book.setAvailable(true);

        when(bookRepository.findByIsbn("9780132350884")).thenReturn(Optional.of(book));

        bookService.delete("9780132350884");

        verify(bookRepository).findByIsbn("9780132350884");

        verify(bookRepository).delete(book);
    }

    //==================================================
    // TESTS - FIND BY ISBN
    //==================================================

    // Success
    @Test
    void findBookByIsbnSuccessfully() {

        Book book = new Book();
        book.setIsbn("9780132350884");
        book.setTitle("Clean Code");

        when(bookRepository.findByIsbn("9780132350884")).thenReturn(Optional.of(book));

        BookResponse result = bookService.findByIsbn("9780132350884");

        assertNotNull(result);
        assertEquals("9780132350884", result.isbn());
        assertEquals("Clean Code", result.title());

        verify(bookRepository).findByIsbn("9780132350884");
    }

    // Error
    @Test
    void findBookByIsbnShouldThrowExceptionWhenNotFound() {

        when(bookRepository.findByIsbn("9780132350884")).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> bookService.findByIsbn("9780132350884"));

        assertEquals("Book not found.", exception.getMessage());

        verify(bookRepository).findByIsbn("9780132350884");
    }

    //==================================================
    // TESTS - FIND BY TITLE
    //==================================================

    // Success
    @Test
    void findBookByTitleSuccessfully() {

        Book book = new Book();
        book.setTitle("Clean Code");

        List<Book> books = List.of(book);

        when(bookRepository.findByTitleContainingIgnoreCase("Code")).thenReturn(books);

        List<BookResponse> result = bookService.findByTitle("Code");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).title());
        assertTrue(result.get(0).available());

        verify(bookRepository).findByTitleContainingIgnoreCase("Code");
    }

    // Error
    @Test
    void findByTitleShouldReturnEmptyListWhenNoBooksAreFound() {

        when(bookRepository.findByTitleContainingIgnoreCase("Code")).thenReturn(List.of());

        List<BookResponse> result = bookService.findByTitle("Code");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(bookRepository).findByTitleContainingIgnoreCase("Code");
    }
}