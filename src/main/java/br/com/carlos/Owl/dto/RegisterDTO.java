package br.com.carlos.Owl.dto;

import br.com.carlos.Owl.enums.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {

}
