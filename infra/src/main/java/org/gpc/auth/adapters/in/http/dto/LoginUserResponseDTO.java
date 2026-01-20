package org.gpc.auth.adapters.in.http.dto;

public record LoginUserResponseDTO(String accessToken, long expiresIn) implements DTO {
}
