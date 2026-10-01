package br.com.carlos.Owl.ControllerTests.BookController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.carlos.Owl.controller.BookController;
import br.com.carlos.Owl.dto.infra.request.BookRequest;
import br.com.carlos.Owl.dto.infra.response.BookResponse;
import br.com.carlos.Owl.repository.UserRepository;
import br.com.carlos.Owl.security.TokenService;
import br.com.carlos.Owl.service.BookService;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerSucessoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    //==================================================
    // TESTS - REGISTER
    //==================================================

    @Test
    void registerBookSuccessfully() throws Exception {

        BookResponse response = new BookResponse("Clean Code", "Robert C. Martin", "9780132350884", "Programming",
                true);

        when(bookService.register(any(BookRequest.class))).thenReturn(response);

        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content("""
                {
                    "title": "Clean Code",
                    "author": "Robert C. Martin",
                    "isbn": "9780132350884",
                    "category": "Programming",
                    "available": true
                }
                """)).andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert C. Martin"))
                .andExpect(jsonPath("$.isbn").value("9780132350884"))
                .andExpect(jsonPath("$.category").value("Programming")).andExpect(jsonPath("$.available").value(true));

        verify(bookService).register(any(BookRequest.class));
    }

    //==================================================
    // TESTS - UPDATE
    //==================================================

    @Test
    void updateBookSuccessfully() throws Exception {

        BookResponse response = new BookResponse("Clean Code", "Robert C. Martin", "9780132350884", "Programming",
                true);

        when(bookService.update(eq("9780132350884"), any(BookRequest.class))).thenReturn(response);

        mockMvc.perform(put("/books/9780132350884").contentType(MediaType.APPLICATION_JSON).content("""
                {
                    "title": "Clean Code",
                    "author": "Robert C. Martin",
                    "isbn": "9780132350884",
                    "category": "Programming",
                    "available": true
                }
                """)).andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert C. Martin"))
                .andExpect(jsonPath("$.isbn").value("9780132350884"))
                .andExpect(jsonPath("$.category").value("Programming")).andExpect(jsonPath("$.available").value(true));

        verify(bookService).update(eq("9780132350884"), any(BookRequest.class));
    }

    //==================================================
    // TESTS - DELETE
    //==================================================

    @Test
    void deleteBookSuccessfully() throws Exception {

        mockMvc.perform(delete("/books/9780132350884")).andExpect(status().isOk()).andExpect(content().string(""));

        verify(bookService).delete("9780132350884");
    }

    //==================================================
    // TESTS - LIST
    //==================================================

    @Test
    void listBooksSuccessfully() throws Exception {

        BookResponse book1 = new BookResponse("Clean Code", "Robert C. Martin", "9780132350884", "Programming", true);

        BookResponse book2 = new BookResponse("Monster", "Sakamoto", "9780132350157", "Manga", true);

        List<BookResponse> books = List.of(book1, book2);

        when(bookService.list()).thenReturn(books);

        mockMvc.perform(get("/books")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("9780132350884"))
                .andExpect(jsonPath("$[1].isbn").value("9780132350157"));

        verify(bookService).list();
    }

    //==================================================
    // TESTS - FIND BY TITLE
    //==================================================

    @Test
    void findBookByTitleSuccessfully() throws Exception {

        BookResponse response = new BookResponse("Clean Code", "Robert C. Martin", "9780132350884", "Programming",
                true);

        List<BookResponse> books = List.of(response);

        when(bookService.findByTitle("Clean")).thenReturn(books);

        mockMvc.perform(get("/books/title/Clean")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"));

        verify(bookService).findByTitle("Clean");
    }

    //==================================================
    // TESTS - FIND BY ISBN
    //==================================================

    @Test
    void findBookByIsbnSuccessfully() throws Exception {

        BookResponse response = new BookResponse("Clean Code", "Robert C. Martin", "9780132350884", "Programming",
                true);

        when(bookService.findByIsbn("9780132350884")).thenReturn(response);

        mockMvc.perform(get("/books/isbn/9780132350884")).andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("9780132350884"))
                .andExpect(jsonPath("$.title").value("Clean Code"));

        verify(bookService).findByIsbn("9780132350884");
    }
}
