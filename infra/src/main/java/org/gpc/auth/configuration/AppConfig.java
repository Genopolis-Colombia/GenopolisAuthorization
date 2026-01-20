package org.gpc.auth.configuration;

import org.gpc.auth.adapters.out.mysql.MysqlUserRepositoryImpl;
import org.gpc.auth.adapters.out.mysql.UserRepository;
import org.gpc.auth.handlers.*;
//import org.gpc.auth.handlers.UpdateUserHandler;
import org.gpc.auth.port.RepositoryPort;
import org.gpc.auth.services.BCryptPasswordHasherService;
import org.gpc.auth.services.JwtService;
import org.gpc.auth.services.JwtServiceImpl;
import org.gpc.auth.services.PasswordHasherService;
import org.gpc.auth.usecase.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class AppConfig {

    @Bean
    PasswordHasherService getBCryptPasswordHasherService() {
        return new BCryptPasswordHasherService();
    }

    @Bean
    JwtService getJwtService(JwtProperties jwtProperties){
        return new JwtServiceImpl(jwtProperties);
    }

    @Bean
    MysqlUserRepositoryImpl getMysqlUserRepositoryImpl(UserRepository userRepository) {
        return new MysqlUserRepositoryImpl(userRepository);
    }

    @Bean
    RegisterUserUseCaseImpl getRegisterUserUseCaseImpl(
            RepositoryPort repositoryPort,
            PasswordHasherService passwordHasherService,
            JwtService jwtService
    ) {
        return new RegisterUserUseCaseImpl(repositoryPort, passwordHasherService, jwtService);
    }

    @Bean
    LoginUserUseCaseImpl getUserByIdUseCaseImpl(
            RepositoryPort repositoryPort,
            PasswordHasherService passwordHasherService,
            JwtService jwtService
    ) {
        return new LoginUserUseCaseImpl(repositoryPort, passwordHasherService, jwtService);
    }

    @Bean
    DeleteUserUseCaseImpl getDeleteUserUseCaseImpl(
            RepositoryPort repositoryPort,
            JwtService jwtService
    ) {
        return new DeleteUserUseCaseImpl(repositoryPort, jwtService);
    }

    @Bean
    UpdateUserUseCaseImpl getUpdateUserUseCaseImpl(
            RepositoryPort repositoryPort,
            JwtService jwtService,
            PasswordHasherService passwordHasherService
    ) {
        return new UpdateUserUseCaseImpl(repositoryPort, jwtService, passwordHasherService);
    }

    @Bean
    ValidateTokenUseCaseImpl getValidateTokenUseCaseImpl(
            RepositoryPort repositoryPort,
            JwtService jwtService
    ){
        return new ValidateTokenUseCaseImpl(repositoryPort, jwtService);
    }

    @Bean
    LoginUserHandler getLoginUserHandler(
            LoginUserUseCaseImpl loginUserUseCase,
            JwtProperties jwtProperties) {
        return new LoginUserHandler(loginUserUseCase, jwtProperties.accessTokenExpirationSeconds());
    }

    @Bean
    ValidateAccessTokenHandler getValidateAccessTokenHandler(
            ValidateTokenUseCaseImpl validateTokenUseCase
    ){
        return new ValidateAccessTokenHandler(validateTokenUseCase);
    }
    @Bean
    RegisterUserHandler getRegisterUserHandler(RegisterUserUseCaseImpl registerUserUseCase) {
        return new RegisterUserHandler(registerUserUseCase);
    }

    @Bean
    UpdateUserHandler getUpdateUserHandler(UpdateUserUseCaseImpl updateUserUseCase) {
        return new UpdateUserHandler(updateUserUseCase);
    }
    @Bean
    DeleteUserHandler getDeleteUserHandler(DeleteUserUseCaseImpl deleteUserUseCase) {
        return new DeleteUserHandler(deleteUserUseCase);
    }

}
