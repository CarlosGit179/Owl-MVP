package br.com.carlos.Owl.ControllerTests.LoanController.sucesso.filtroOn;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.carlos.Owl.controller.LoanController;
import br.com.carlos.Owl.dto.infra.response.BookResponse;
import br.com.carlos.Owl.dto.infra.response.LoanResponse;
import br.com.carlos.Owl.dto.infra.response.StudentResponse;
import br.com.carlos.Owl.entity.Student;
import br.com.carlos.Owl.entity.User;
import br.com.carlos.Owl.enums.UserRole;
import br.com.carlos.Owl.repository.UserRepository;
import br.com.carlos.Owl.security.TokenService;
import br.com.carlos.Owl.service.LoanService;

@WebMvcTest(LoanController.class)
@AutoConfigureMockMvc
public class FindMyLoansTeste {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void findMyLoansSuccessfully() throws Exception {

        Student student = new Student();
        student.setName("Carlos");
        student.setRegistrationNumber("123456");

        User user = new User("carlos", "password", UserRole.STUDENT, student);

        Authentication authentication = new TestingAuthenticationToken(user, null, user.getAuthorities());

        StudentResponse studentResponse = new StudentResponse("Carlos", "123456");

        BookResponse bookResponse = new BookResponse("Clean Code", "Robert C. Martin", "9780132350884", "Programming",
                true);

        LoanResponse loan = new LoanResponse(studentResponse, bookResponse, "2026-09-24", "2026-10-24", "Active");

        when(loanService.findByStudentRegistrationNumber("123456")).thenReturn(List.of(loan));

        mockMvc.perform(get("/loans/me").with(authentication(authentication))).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].student.name").value("Carlos"))
                .andExpect(jsonPath("$[0].student.registrationNumber").value("123456"))
                .andExpect(jsonPath("$[0].book.title").value("Clean Code"))
                .andExpect(jsonPath("$[0].book.isbn").value("9780132350884"))
                .andExpect(jsonPath("$[0].status").value("Active"));

        verify(loanService).findByStudentRegistrationNumber("123456");
    }
}
