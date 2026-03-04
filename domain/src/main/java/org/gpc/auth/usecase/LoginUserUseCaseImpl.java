package org.gpc.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.gpc.auth.error.InvalidCredentialsException;
import org.gpc.auth.kernel.LoginUser;
import org.gpc.auth.kernel.User;
import org.gpc.auth.port.RepositoryPort;
import org.gpc.auth.services.JwtService;
import org.gpc.auth.services.PasswordHasherService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class LoginUserUseCaseImpl {

    private final RepositoryPort repositoryPort;
    private final PasswordHasherService passwordHasherService;
    private final JwtService jwtService;

    public Optional<String> execute(LoginUser loginUser) throws InvalidCredentialsException {
        Optional<User> maybeUser = repositoryPort.findByUsername(loginUser.username());
        if(maybeUser.isEmpty())
            maybeUser = repositoryPort.findByEmail(loginUser.username());
        if (maybeUser.isEmpty())
            return Optional.empty();
        else {
            User user = maybeUser.get();
            if (passwordHasherService.matches(loginUser.password(), user.passwordHash())) {
                Map<String, Object> customClaims = jwtService.customClaims(
                        user.username(),
                        user.role().name()
                );

                return Optional.of(
                        jwtService.generateToken(
                                user.id().toString(),
                                customClaims
                        )
                );
            } else throw new InvalidCredentialsException();
        }
    }
}