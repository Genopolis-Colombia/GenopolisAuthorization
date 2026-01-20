package org.gpc.auth.handlers;

import lombok.RequiredArgsConstructor;
import org.gpc.auth.adapters.in.http.dto.*;
import org.gpc.auth.error.InvalidCredentialsException;
import org.gpc.auth.kernel.LoginUser;
import org.gpc.auth.usecase.LoginUserUseCaseImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RequiredArgsConstructor
public class LoginUserHandler {

    private final LoginUserUseCaseImpl loginUserUseCase;
    private final long expirationToken;

    public ResponseEntity<DTO> handle(LoginUserDTO dto) throws InvalidCredentialsException {

        return loginUserUseCase.execute(new LoginUser(dto.username(), dto.password()))
                .map(token -> new ResponseEntity<DTO>(
                        new LoginUserResponseDTO(token, expirationToken)
                        , HttpStatus.OK)
                )
                .orElseGet(() -> new ResponseEntity<>(
                        new ErrorResponse("User not found", "The user " + dto.username() + " was not found"),
                        HttpStatus.NOT_FOUND));
    }
}