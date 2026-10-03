package br.com.carlos.Owl.dto.security;

import br.com.carlos.Owl.enums.UserRole;

public record RegisterDTO(String login, String password, UserRole role, String registrationNumber) {

}
