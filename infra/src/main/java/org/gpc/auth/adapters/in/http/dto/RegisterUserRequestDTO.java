package org.gpc.auth.adapters.in.http.dto;

public record RegisterUserRequestDTO(String username, String email, String password, String role) implements DTO {
}