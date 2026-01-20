package org.gpc.auth.usecase;

import org.gpc.auth.kernel.UserRegistration;
import org.gpc.auth.port.RepositoryPort;
import org.gpc.auth.services.JwtService;
import org.gpc.auth.services.PasswordHasherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

public class RegisterUserUseCaseImpl implements UseCase<UserRegistration, String> {

    private static final Logger logger = LoggerFactory.getLogger(RegisterUserUseCaseImpl.class);
    private final RepositoryPort repositoryPort;
    private final PasswordHasherService passwordHasherService;
    private final JwtService jwtService;

    public RegisterUserUseCaseImpl(
            RepositoryPort repositoryPort,
            PasswordHasherService passwordHasherService,
            JwtService jwtService
    ) {
        this.repositoryPort = repositoryPort;
        this.passwordHasherService = passwordHasherService;
        this.jwtService = jwtService;
    }

    @Override
    public String execute(UserRegistration userRegistration) {
        logger.debug("Registering user {}", userRegistration.username());
        String passwordHash = passwordHasherService.hash(userRegistration.rawPassword());
        UUID createdUserId = repositoryPort.saveUser(userRegistration, passwordHash);
        Map<String, Object> customClaims = jwtService.customClaims(
                userRegistration.username(),
                userRegistration.role().name()
        );

        return jwtService.generateToken(
                createdUserId.toString(),
                customClaims
        );
    }
}