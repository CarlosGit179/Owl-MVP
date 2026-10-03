package br.com.carlos.Owl.dto.infra.response;

public record BookResponse(String title, String author, String isbn, String category, Boolean available) {

}
