package br.com.carlos.Owl.dto.infra.request;

public record LoanRequest(StudentRequest student, BookRequest book, String dueDate, String returnDate, String status) {

}
