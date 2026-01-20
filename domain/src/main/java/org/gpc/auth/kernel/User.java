package org.gpc.auth.kernel;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record User(
        UUID id,
        String username,
        String email,
        String passwordHash,
        Role role,
        Instant createdAt,
        Instant updatedAt
) {

    public User {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(email, "email must not be null");
        Objects.requireNonNull(role, "role must not be null");
        Objects.requireNonNull(createdAt, "createdAt must not be null");
        Objects.requireNonNull(updatedAt, "updatedAt must not be null");
    }
}