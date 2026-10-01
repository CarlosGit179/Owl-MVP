package br.com.carlos.Owl.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.carlos.Owl.repository.UserRepository;
import br.com.carlos.Owl.service.AuthorizationService;

@ExtendWith(MockitoExtension.class)
public class AuthorizationTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private UserRepository userRepository;

    @Test
    void loadUserByUsernameComSucesso() {

        UserDetails user = mock(UserDetails.class);

        when(userRepository.findByLogin("carlos")).thenReturn(user);

        UserDetails resultado = authorizationService.loadUserByUsername("carlos");

        assertNotNull(resultado);
        assertEquals(user, resultado);

        verify(userRepository).findByLogin("carlos");
    }

}
