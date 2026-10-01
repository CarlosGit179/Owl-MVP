package br.com.carlos.Owl.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.carlos.Owl.dto.infra.request.BookRequest;
import br.com.carlos.Owl.dto.infra.response.BookResponse;
import br.com.carlos.Owl.entity.Book;
import br.com.carlos.Owl.exception.Books.BookHasActiveLoanException;
import br.com.carlos.Owl.exception.Books.BookNotFoundException;
import br.com.carlos.Owl.exception.Books.IsbnAlreadyRegisteredException;
import br.com.carlos.Owl.repository.BookRepository;
import lombok.RequiredArgsConstructor;

/**
    * Applies business rules for book operations.
    *<p>
    * Supports registering, listing, finding, updating, and deleting books.
    *</p>
    */
@Service
@RequiredArgsConstructor
public class BookService {

    /**
    * Book repository.
    */
    private final BookRepository bookRepository;

    private BookResponse toBookResponse(Book book) {
        return new BookResponse(book.getTitle(), book.getAuthor(), book.getIsbn(), book.getCategory(),
                book.isAvailable());
    }

    /**
     * Registers a new book.
     *
     * @param request Book data to be registered.
     * @return The registered book.
     * @throws IsbnAlreadyRegisteredException If the book's ISBN is already registered.
     */
    public BookResponse register(BookRequest request) {
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new IsbnAlreadyRegisteredException();
        }

        Book book = new Book();
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setIsbn(request.isbn());
        book.setPublisher(request.publisher());
        book.setPublicationYear(request.publicationYear());
        book.setCategory(request.category());
        book.setAvailable(true);

        return toBookResponse(bookRepository.save(book));
    }

    /**
     * Lists all books.
     *
     * @return A list of books.
     */
    public List<BookResponse> list() {
        return bookRepository.findAll().stream().map(this::toBookResponse).toList();
    }

    /**
     * Finds a book by ISBN.
     *
     * @param isbn ISBN of the book.
     * @return The book found.
     * @throws BookNotFoundException If the book is not found.
     */
    public BookResponse findByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn).orElseThrow(BookNotFoundException::new);

        return toBookResponse(book);
    }

    /**
     * Finds books by title.
     *
     * @param title Title of the books to search for.
     * @return A list of books found.
     */
    public List<BookResponse> findByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream().map(this::toBookResponse).toList();
    }

    /**
     * Updates an existing book.
     *
     * @param isbn ISBN of the book to update.
     * @param request Updated book data.
     * @return The updated book.
     * @throws BookNotFoundException If the book is not found.
     */
    public BookResponse update(String isbn, BookRequest request) {

        Book book = bookRepository.findByIsbn(isbn).orElseThrow(BookNotFoundException::new);

        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setIsbn(request.isbn());
        book.setPublisher(request.publisher());
        book.setPublicationYear(request.publicationYear());
        book.setCategory(request.category());

        return toBookResponse(bookRepository.save(book));
    }

    /**
     * Deletes an existing book.
     *
     * @param isbn ISBN of the book to delete.
     * @throws BookNotFoundException If the book is not found.
     * @throws BookHasActiveLoanException If the book has an active loan.
     */
    public void delete(String isbn) {

        Book book = bookRepository.findByIsbn(isbn).orElseThrow(BookNotFoundException::new);

        if (!book.isAvailable()) {
            throw new BookHasActiveLoanException();
        }

        bookRepository.delete(book);
    }
}
