package org.gpc.auth.adapters.in.http.dto;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        String email,
        String role,
        Instant createdAt,
        Instant updatedAt
) implements DTO {
}