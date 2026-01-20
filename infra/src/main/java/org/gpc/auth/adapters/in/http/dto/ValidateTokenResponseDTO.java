package org.gpc.auth.adapters.in.http.dto;

import org.gpc.auth.kernel.Role;

import java.util.UUID;

public record ValidateTokenResponseDTO(
        UUID id,
        String username,
        String email,
        Role role
) implements DTO{
}
