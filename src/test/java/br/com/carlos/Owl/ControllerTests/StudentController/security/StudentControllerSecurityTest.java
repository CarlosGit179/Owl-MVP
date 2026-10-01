package br.com.carlos.Owl.ControllerTests.StudentController.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.carlos.Owl.controller.StudentController;
import br.com.carlos.Owl.dto.infra.request.StudentRequest;
import br.com.carlos.Owl.entity.User;
import br.com.carlos.Owl.enums.UserRole;
import br.com.carlos.Owl.repository.UserRepository;
import br.com.carlos.Owl.security.TokenService;
import br.com.carlos.Owl.service.LoanService;
import br.com.carlos.Owl.service.StudentService;

@WebMvcTest(StudentController.class)
public class StudentControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private LoanService loanService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Test
    void studentCannotRegisterStudent() throws Exception {

        User user = new User("Carlos", "123", UserRole.STUDENT, null);

        Authentication authentication = new TestingAuthenticationToken(user, null, user.getAuthorities());

        mockMvc.perform(post("/students").with(authentication(authentication)).contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Carlos",
                            "registrationNumber": "123"
                        }
                        """)).andExpect(status().isForbidden());

        verify(studentService, never()).register(any(StudentRequest.class));
    }

}
