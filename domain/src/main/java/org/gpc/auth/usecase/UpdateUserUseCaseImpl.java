package org.gpc.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.gpc.auth.error.InvalidTokenException;
import org.gpc.auth.kernel.User;
import org.gpc.auth.kernel.UserUpdate;
import org.gpc.auth.port.RepositoryPort;
import org.gpc.auth.services.JwtService;
import org.gpc.auth.services.PasswordHasherService;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class UpdateUserUseCaseImpl {

    private final RepositoryPort repositoryPort;
    private final JwtService jwtService;
    private final PasswordHasherService passwordHasherService;

    public Optional<User> execute(UserUpdate update) throws InvalidTokenException {
        String userId = jwtService.verify(update.accessToken());
        return repositoryPort.findById(UUID.fromString(userId))
                .map(user -> new User(
                        user.id(),
                        update.username().orElseGet(user::username),
                        update.email().orElseGet(user::email),
                        update.password().map(passwordHasherService::hash).orElseGet(user::passwordHash),
                        user.role(),
                        user.createdAt(),
                        user.updatedAt()
                ))
                .flatMap(repositoryPort::updateUser);
    }
}