package br.com.carlos.Owl.controllerTests.LoanController.security;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.carlos.Owl.controller.LoanController;
import br.com.carlos.Owl.entity.User;
import br.com.carlos.Owl.enums.UserRole;
import br.com.carlos.Owl.repository.UserRepository;
import br.com.carlos.Owl.security.TokenService;
import br.com.carlos.Owl.service.LoanService;

@WebMvcTest(LoanController.class)
public class LoanControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Test 
    void studentCannotAccessLoanFeatures() throws Exception {

        User user = new User("Carlos", "123", UserRole.STUDENT, null);

        Authentication authentication = new TestingAuthenticationToken(user, null, user.getAuthorities());

        mockMvc.perform(post("/loans").with(authentication(authentication)).param("studentRegistrationNumber", "123456").param("bookIsbn", "9780132350884"))
        .andExpect(status().isForbidden());

        verify(loanService, never()).createLoan("123456", "9780132350884");
    }

}
