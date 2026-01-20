package org.gpc.auth.handlers;

import lombok.RequiredArgsConstructor;
import org.gpc.auth.adapters.in.http.dto.DTO;
import org.gpc.auth.adapters.in.http.dto.DeleteUserRequestDTO;
import org.gpc.auth.adapters.in.http.dto.ErrorResponse;
import org.gpc.auth.error.InvalidTokenException;
import org.gpc.auth.usecase.DeleteUserUseCaseImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class DeleteUserHandler {

    private final DeleteUserUseCaseImpl deleteUserUseCase;

    public ResponseEntity<DTO> handle(DeleteUserRequestDTO requestDTO) throws InvalidTokenException {
        return deleteUserUseCase.execute(requestDTO.accessToken())
                .map(updatedUser -> new ResponseEntity<DTO>(HttpStatus.NO_CONTENT))
                .orElseGet(() -> new ResponseEntity<>(
                        new ErrorResponse("Invalid user id", "The provided user was not found"),
                        HttpStatus.NOT_FOUND));
    }
}