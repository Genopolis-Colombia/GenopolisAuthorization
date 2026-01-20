package org.gpc.auth.handlers;

import lombok.RequiredArgsConstructor;
import org.gpc.auth.adapters.in.http.dto.*;
import org.gpc.auth.error.InvalidTokenException;
import org.gpc.auth.usecase.ValidateTokenUseCaseImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class ValidateAccessTokenHandler {

    private final ValidateTokenUseCaseImpl validateTokenUseCase;

    public ResponseEntity<DTO> handle(ValidateTokenDTO validateTokenDTO) throws InvalidTokenException {
        return validateTokenUseCase
                .execute(validateTokenDTO.accessToken())
                .map(user -> new ResponseEntity<DTO>(
                        new ValidateTokenResponseDTO(
                                user.id(),
                                user.username(),
                                user.email(),
                                user.role())
                        , HttpStatus.OK)
                )
                .orElseGet(() -> new ResponseEntity<>(
                        new ErrorResponse("User not found", "The token is valid but the user was not found"),
                        HttpStatus.NOT_FOUND));
    }
}
