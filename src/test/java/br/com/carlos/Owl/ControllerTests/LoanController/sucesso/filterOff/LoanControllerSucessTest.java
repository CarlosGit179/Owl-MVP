package br.com.carlos.Owl.controllerTests.LoanController.sucesso.filterOff;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.carlos.Owl.controller.LoanController;
import br.com.carlos.Owl.dto.infra.response.BookResponse;
import br.com.carlos.Owl.dto.infra.response.LoanResponse;
import br.com.carlos.Owl.dto.infra.response.StudentResponse;
import br.com.carlos.Owl.enums.LoanStatus;
import br.com.carlos.Owl.repository.UserRepository;
import br.com.carlos.Owl.security.TokenService;
import br.com.carlos.Owl.service.LoanService;

@WebMvcTest(LoanController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LoanControllerSucessTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private Authentication authentication;

    //==================================================
    // TESTS - CREATE LOAN
    //==================================================

    @Test
    void createLoanSuccessfully() throws Exception {

        StudentResponse student = new StudentResponse("Carlos", "123456");

        BookResponse book = new BookResponse("Clean Code", "Robert C. Martin", "9780132350884", "Programming", true);

        LoanResponse loan = new LoanResponse(student, book, "2026-09-24", "2026-10-24", "Active");

        when(loanService.createLoan("123456", "9780132350884")).thenReturn(loan);

        mockMvc.perform(post("/loans").param("studentRegistrationNumber", "123456").param("bookIsbn", "9780132350884"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.student.registrationNumber").value("123456"))
                .andExpect(jsonPath("$.book.isbn").value("9780132350884"))
                .andExpect(jsonPath("$.status").value("Active"));

        verify(loanService).createLoan("123456", "9780132350884");
    }

    //==================================================
    // TESTS - RETURN LOAN
    //==================================================

    @Test
    void returnLoanSuccessfully() throws Exception {

        StudentResponse student = new StudentResponse("Carlos", "123456");

        BookResponse book = new BookResponse("Clean Code", "Robert C. Martin", "9780132350884", "Programming", true);

        LoanResponse loan = new LoanResponse(student, book, "2026-09-24", "2026-10-24", "Returned");

        when(loanService.returnLoan("123456")).thenReturn(loan);

        mockMvc.perform(put("/loans/123456/return")).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Returned"));

        verify(loanService).returnLoan("123456");
    }

    //==================================================
    // TESTS - LIST
    //==================================================

    @Test
    void listLoansSuccessfully() throws Exception {

        StudentResponse student1 = new StudentResponse("Carlos", "123456");

        BookResponse book1 = new BookResponse("Clean Code", "Robert C. Martin", "9780132350884", "Programming", true);

        StudentResponse student2 = new StudentResponse("Maria", "654321");

        BookResponse book2 = new BookResponse("Monster", "Sakamoto", "9780132350157", "Manga", true);

        LoanResponse loan1 = new LoanResponse(student1, book1, "2026-09-24", "2026-10-24", "Returned");

        LoanResponse loan2 = new LoanResponse(student2, book2, "2026-09-24", "2026-10-24", "Returned");

        List<LoanResponse> loans = List.of(loan1, loan2);

        when(loanService.list()).thenReturn(loans);

        mockMvc.perform(get("/loans")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].student.registrationNumber").value("123456"))
                .andExpect(jsonPath("$[1].student.registrationNumber").value("654321"));

        verify(loanService).list();
    }

    //==================================================
    // TESTS - FIND BY REGISTRATION NUMBER
    //==================================================

    @Test
    void findLoansByRegistrationNumberSuccessfully() throws Exception {

        StudentResponse student = new StudentResponse("Carlos", "123456");

        BookResponse book = new BookResponse("Clean Code", "Robert C. Martin", "9780132350884", "Programming", true);

        LoanResponse loan = new LoanResponse(student, book, "2026-09-24", "2026-10-24", "Returned");

        List<LoanResponse> loans = List.of(loan);

        when(loanService.findByStudentRegistrationNumber("123456")).thenReturn(loans);

        mockMvc.perform(get("/loans/registration-number/123456")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].student.registrationNumber").value("123456"));

        verify(loanService).findByStudentRegistrationNumber("123456");
    }

    //==================================================
    // TESTS - FIND BY STATUS
    //==================================================

    @Test
    void findLoansByStatusSuccessfully() throws Exception {

        StudentResponse student = new StudentResponse("Carlos", "123456");

        BookResponse book = new BookResponse("Clean Code", "Robert C. Martin", "9780132350884", "Programming", true);

        LoanResponse loan = new LoanResponse(student, book, "2026-09-24", "2026-10-24", "Returned");

        List<LoanResponse> loans = List.of(loan);

        when(loanService.findByStatus(LoanStatus.RETURNED)).thenReturn(loans);

        mockMvc.perform(get("/loans/status/RETURNED")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("Returned"));

        verify(loanService).findByStatus(LoanStatus.RETURNED);
    }

}
