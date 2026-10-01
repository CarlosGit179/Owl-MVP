package br.com.carlos.Owl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.carlos.Owl.dto.security.AuthenticationDTO;
import br.com.carlos.Owl.dto.security.LoginResponseDTO;
import br.com.carlos.Owl.dto.security.RegisterDTO;
import br.com.carlos.Owl.entity.Student;
import br.com.carlos.Owl.entity.User;
import br.com.carlos.Owl.enums.UserRole;
import br.com.carlos.Owl.repository.StudentRepository;
import br.com.carlos.Owl.repository.UserRepository;
import br.com.carlos.Owl.security.TokenService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {

        if (data.role() != UserRole.STUDENT) {
            return ResponseEntity.badRequest().build();
        }

        if (userRepository.findByLogin(data.login()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        Student student = null;

        if (data.role() == UserRole.STUDENT) {

            if (data.registrationNumber() == null) {
                return ResponseEntity.badRequest().build();
            }

            if (userRepository.existsByStudentRegistrationNumber(data.registrationNumber())) {
                return ResponseEntity.badRequest().build();
            }

            student = studentRepository.findByRegistrationNumber(data.registrationNumber());

            if (student == null) {
                return ResponseEntity.badRequest().build();
            }
        }

        User newUser = new User(data.login(), encryptedPassword, data.role(), student);

        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
