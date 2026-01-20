package org.gpc.auth.adapters.in.http;

import org.gpc.auth.MySQLTestContainer;
import org.gpc.auth.adapters.in.http.dto.*;
import org.gpc.auth.kernel.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerAdapterTest extends MySQLTestContainer {

    @Value("${local.server.port}")
    private Integer port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String host = "http://localhost:";

    @Test
    void shouldRegisterANewUserSuccessful() {
        String path = host + port + "/auth/register";
        String validationPath = host + port + "/auth/validate";
        RegisterUserRequestDTO entity = new RegisterUserRequestDTO("Moris", "moris@correo", "dog", "USER");
        HttpEntity<RegisterUserRequestDTO> request = new HttpEntity<>(entity);
        ResponseEntity<RegisterUserResponseDTO> response = restTemplate.exchange(path, HttpMethod.POST, request, RegisterUserResponseDTO.class);

        ValidateTokenDTO validateTokenDTO = new ValidateTokenDTO(response.getBody().accessToken());
        HttpEntity<ValidateTokenDTO> validateRequest = new HttpEntity<>(validateTokenDTO);
        ResponseEntity<ValidateTokenResponseDTO> validateTokenResponseDTO = restTemplate.exchange(validationPath, HttpMethod.POST, validateRequest, ValidateTokenResponseDTO.class);
        Optional<User> maybeUserRegistered = mySQLAuthRepository.findById(Objects.requireNonNull(validateTokenResponseDTO.getBody().id()));

        validateSuccessfulResponse(response);
        assertTrue(maybeUserRegistered.isPresent());
        assertEquals(entity.username(), maybeUserRegistered.get().username());
        assertEquals(entity.email(), maybeUserRegistered.get().email());
        assertEquals(entity.role().toUpperCase(), maybeUserRegistered.get().role().toString());
    }

    private <T> void validateSuccessfulResponse(ResponseEntity<T> response) {
        System.out.println("Response: " + response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

}