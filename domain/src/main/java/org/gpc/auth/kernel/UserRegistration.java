package org.gpc.auth.kernel;

import java.util.Objects;

public record UserRegistration(
        String username,
        String email,
        String rawPassword,
        Role role
) {
    public UserRegistration {
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(email, "email must not be null");
        Objects.requireNonNull(rawPassword, "rawPassword must not be null");
        Objects.requireNonNull(role, "role must not be null");

        if (username.isBlank()) {
            throw new IllegalArgumentException("username must not be blank");
        }
        if (email.isBlank()) {
            throw new IllegalArgumentException("email must not be blank");
        }
        if (rawPassword.isBlank()) {
            throw new IllegalArgumentException("rawPassword must not be blank");
        }
    }
}