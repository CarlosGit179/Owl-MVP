package br.com.carlos.Owl.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.carlos.Owl.entity.User;
import br.com.carlos.Owl.enums.UserRole;
import br.com.carlos.Owl.repository.UserRepository;

@Configuration
public class AdminInitializer {

    @Value("${app.admin.login}")
    private String adminLogin;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByLogin("admin") == null) {
                User admin = new User(adminLogin, passwordEncoder.encode(adminPassword), UserRole.ADMIN);

                userRepository.save(admin);
            }
        };

    }
}
