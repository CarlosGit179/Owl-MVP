package br.com.carlos.Owl.dto.infra.response;

public record LoanResponse(StudentResponse student, BookResponse book, String loanDate, String dueDate, String status) {

}
