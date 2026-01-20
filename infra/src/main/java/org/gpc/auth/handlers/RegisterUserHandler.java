package org.gpc.auth.handlers;

import lombok.RequiredArgsConstructor;
import org.gpc.auth.adapters.in.http.dto.DTO;
import org.gpc.auth.adapters.in.http.dto.RegisterUserRequestDTO;
import org.gpc.auth.adapters.in.http.dto.RegisterUserResponseDTO;
import org.gpc.auth.adapters.in.http.dto.ErrorResponse;
import org.gpc.auth.kernel.Role;
import org.gpc.auth.kernel.UserRegistration;
import org.gpc.auth.usecase.RegisterUserUseCaseImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RequiredArgsConstructor
public class RegisterUserHandler implements Handler<RegisterUserRequestDTO, ResponseEntity<DTO>> {

  private final RegisterUserUseCaseImpl registerUserUseCase;

  @Override
  public ResponseEntity<DTO> handle(RegisterUserRequestDTO request) {
    Role role;
    try {
      role = Role.valueOf(request.role().toUpperCase());
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.badRequest()
              .body(new ErrorResponse("Invalid role", "Role " + request.role() + " is not valid"));
    }

    String token = registerUserUseCase.execute(new UserRegistration(
            request.username(),
            request.email(),
            request.password(),
            role
    ));

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(new RegisterUserResponseDTO(token));
  }
}