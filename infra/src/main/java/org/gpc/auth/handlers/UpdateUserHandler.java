package org.gpc.auth.handlers;

import lombok.RequiredArgsConstructor;
import org.gpc.auth.adapters.in.http.dto.DTO;
import org.gpc.auth.adapters.in.http.dto.ErrorResponse;
import org.gpc.auth.adapters.in.http.dto.UpdateUserRequestDTO;
import org.gpc.auth.error.InvalidTokenException;
import org.gpc.auth.kernel.UserUpdate;
import org.gpc.auth.usecase.UpdateUserUseCaseImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class UpdateUserHandler {

    private final UpdateUserUseCaseImpl updateUserUseCase;

    public ResponseEntity<DTO> handle(UpdateUserRequestDTO request) throws InvalidTokenException {

        Optional<String> maybeUsername = request.username().filter(nonBlank());
        Optional<String> maybeEmail = request.email().filter(nonBlank());
        Optional<String> maybeRawPassword = request.password().filter(nonBlank());

        Optional<DTO> validation = validateInput(maybeUsername, maybeEmail, maybeRawPassword);
        if (validation.isPresent()) {
            return ResponseEntity.badRequest().body(validation.get());
        }

        return updateUserUseCase.execute(new UserUpdate(
                        request.accessToken(),
                        request.username(),
                        request.email(),
                        request.password()
                ))
                .map(updatedUser -> new ResponseEntity<DTO>(HttpStatus.NO_CONTENT))
                .orElseGet(() -> new ResponseEntity<>(
                        new ErrorResponse("Invalid user id", "The provided user was not found"),
                        HttpStatus.NOT_FOUND));
    }

    private Optional<DTO> validateInput(Optional<String> username,
                                        Optional<String> email,
                                        Optional<String> rawPassword
    ) {
        boolean hasUpdates = username.isPresent() || email.isPresent() || rawPassword.isPresent();

        if (!hasUpdates) {
            return Optional.of(new ErrorResponse("Invalid request",
                    "No field was provided for update"));
        }
        return Optional.empty();
    }


    private Predicate<String> nonBlank() {
        return value -> !value.trim().isEmpty();
    }
}
