package org.gpc.auth.adapters.in.http;

import lombok.RequiredArgsConstructor;
import org.gpc.auth.adapters.in.http.dto.*;
import org.gpc.auth.error.InvalidCredentialsException;
import org.gpc.auth.error.InvalidTokenException;
import org.gpc.auth.handlers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthControllerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(AuthControllerAdapter.class);

  private final RegisterUserHandler registerUserHandler;
  private final LoginUserHandler loginUserHandler;
  private final UpdateUserHandler updateUserHandler;
  private final DeleteUserHandler deleteUserHandler;
  private final ValidateAccessTokenHandler validateAccessTokenHandler;

  @PostMapping("/register")
  public ResponseEntity<DTO> register(@RequestBody RegisterUserRequestDTO request) {
    logger.debug("Registering user {}", request.username());
    return registerUserHandler.handle(request);
  }

  @PostMapping("/login")
  public ResponseEntity<DTO> login(@RequestBody LoginUserDTO request) throws InvalidCredentialsException {
    logger.debug("Login user {}", request.username());
    return loginUserHandler.handle(request);
  }

  @PostMapping("/validate")
  public ResponseEntity<DTO> validate(@RequestBody ValidateTokenDTO request) throws InvalidTokenException {
    logger.debug("Validating access token {}", request.accessToken());
    return validateAccessTokenHandler.handle(request);
  }

  @PostMapping("/update")
  public ResponseEntity<DTO> update(@RequestBody UpdateUserRequestDTO request) throws InvalidTokenException {
    logger.debug("Updating user {}", request.email());
    return updateUserHandler.handle(request);
  }

  @PostMapping("/delete")
  public ResponseEntity<DTO> delete(@RequestBody DeleteUserRequestDTO request) throws InvalidTokenException {
    logger.debug("Start delete process");
    return deleteUserHandler.handle(request);
  }

}