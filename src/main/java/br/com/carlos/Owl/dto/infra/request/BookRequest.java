package br.com.carlos.Owl.dto.infra.request;

public record BookRequest(String title, String author, String isbn, Integer publicationYear, String category,
        String publisher) {

}
