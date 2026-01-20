package org.gpc.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.gpc.auth.error.InvalidTokenException;
import org.gpc.auth.kernel.User;
import org.gpc.auth.port.RepositoryPort;
import org.gpc.auth.services.JwtService;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DeleteUserUseCaseImpl {

    private final RepositoryPort repositoryPort;
    private final JwtService jwtService;

    public Optional<User> execute(String accessToken) throws InvalidTokenException {
        String userId = jwtService.verify(accessToken);
        return repositoryPort.findById(UUID.fromString(userId))
                .map(user -> {
                    repositoryPort.deleteUser(user.id());
                    return user;
                });
    }
}