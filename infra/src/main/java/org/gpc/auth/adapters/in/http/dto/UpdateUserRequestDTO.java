package org.gpc.auth.adapters.in.http.dto;

import java.util.Optional;

public record UpdateUserRequestDTO(
        String accessToken,
        Optional<String> username,
        Optional<String> email,
        Optional<String> password
) implements DTO { }
