package br.com.carlos.Owl.ControllerTests.AuthController.filterOff;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.carlos.Owl.controller.AuthenticationController;
import br.com.carlos.Owl.entity.Student;
import br.com.carlos.Owl.entity.User;
import br.com.carlos.Owl.enums.UserRole;
import br.com.carlos.Owl.repository.StudentRepository;
import br.com.carlos.Owl.repository.UserRepository;
import br.com.carlos.Owl.security.TokenService;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerSuccessTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean 
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private StudentRepository studentRepository;

    @MockitoBean
    private TokenService tokenService;

    @Test 
    void sucessfulLoginTest() throws Exception {
        
        User user = new User("Carlos", "123", UserRole.STUDENT);

        Authentication authentication = new TestingAuthenticationToken(user, null, user.getAuthorities());

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(tokenService.generateToken(user)).thenReturn("fake-jwt-token");

        mockMvc.perform(
            post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "login": "Carlos",
                                "password": "123"
                            }
                            """)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("fake-jwt-token"));

        verify(authenticationManager)
            .authenticate(any());

        verify(tokenService)
            .generateToken(user);
        }

    @Test
    void successfulRegisterTest() throws Exception {

        Student student = new Student();
        student.setName("Carlos");
        student.setRegistrationNumber("123456");

        when(userRepository.findByLogin("Carlos")).thenReturn(null);
        when(userRepository.existsByStudentRegistrationNumber("123456")).thenReturn(false);
        when(studentRepository.findByRegistrationNumber("123456")).thenReturn(student);

        mockMvc.perform(
            post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "login": "Carlos",
                                "password": "123",
                                "role": "STUDENT",
                                "registrationNumber": "123456"
                            }
                            """)
        )
            .andExpect(status().isOk());

        verify(userRepository).save(any(User.class));
        verify(studentRepository).findByRegistrationNumber("123456");
        verify(userRepository).findByLogin("Carlos");
    }

}
