package org.gpc.auth.kernel;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public record UserUpdate(
        String accessToken,
        Optional<String> username,
        Optional<String> email,
        Optional<String> password
) {
    public UserUpdate {
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(email, "email must not be null");
        Objects.requireNonNull(password, "passwordHash must not be null");

        if (username.isPresent() && username.get().isBlank()) {
            throw new IllegalArgumentException("username must not be blank");
        }
        if (email.isPresent() && email.get().isBlank()) {
            throw new IllegalArgumentException("email must not be blank");
        }
        if (password.isPresent() && password.get().isBlank()) {
            throw new IllegalArgumentException("passwordHash must not be blank");
        }
    }
}