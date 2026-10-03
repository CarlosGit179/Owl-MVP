package br.com.carlos.Owl.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.carlos.Owl.dto.infra.response.LoanResponse;
import br.com.carlos.Owl.entity.User;
import br.com.carlos.Owl.enums.LoanStatus;
import br.com.carlos.Owl.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
@Tag(name = "Loan", description = "API for managing loans")
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    @Operation(summary = "Create loan", description = "Creates a loan for a student")
    public LoanResponse createLoan(@RequestParam String studentRegistrationNumber, @RequestParam String bookIsbn) {
        return loanService.createLoan(studentRegistrationNumber, bookIsbn);
    }

    @PutMapping("/{registrationNumber}/return")
    @Operation(summary = "Return book", description = "Records the return of a borrowed book")
    public LoanResponse returnLoan(@PathVariable String registrationNumber) {
        return loanService.returnLoan(registrationNumber);
    }

    @GetMapping
    @Operation(summary = "List all loans", description = "Returns a list of all loans")
    public List<LoanResponse> list() {
        return loanService.list();
    }

    @GetMapping("/registration-number/{registrationNumber}")
    @Operation(summary = "Find loans by registration number", description = "Finds loans by student registration number")
    public List<LoanResponse> findByRegistrationNumber(@PathVariable String registrationNumber) {
        return loanService.findByStudentRegistrationNumber(registrationNumber);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Find loans by status", description = "Finds loans by status")
    public List<LoanResponse> findByStatus(@PathVariable LoanStatus status) {
        return loanService.findByStatus(status);
    }

    @GetMapping("/me")
    @Operation(summary = "Find my loans", description = "Returns the loans of the authenticated student")
    public List<LoanResponse> findMyLoans(Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        String registrationNumber = user.getStudent().getRegistrationNumber();

        return loanService.findByStudentRegistrationNumber(registrationNumber);
    }
}
