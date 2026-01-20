package org.gpc.auth.adapters.out.mysql;

import org.gpc.auth.MySQLTestContainer;
import org.gpc.auth.kernel.User;
import org.gpc.auth.kernel.Role;
import org.gpc.auth.kernel.UserRegistration;
import org.gpc.auth.services.BCryptPasswordHasherService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
class MysqlUserRepositoryImplTest extends MySQLTestContainer {
    BCryptPasswordHasherService bCryptPasswordHasherService = new BCryptPasswordHasherService();

    @Test
    void saveAuthUser() {
        String username = "Alaska";
        String email = "alaska@correo.com";
        Role role = Role.USER;
        String rawPassword = "password";
        String passwordHash = bCryptPasswordHasherService.hash(rawPassword);
        UUID id = mySQLAuthRepository.saveUser(new UserRegistration(username, email, rawPassword, role), passwordHash);
        Optional<User> maybeAuthUser = mySQLAuthRepository.findById(id);
        assert (maybeAuthUser.isPresent());
        assertEquals(username, maybeAuthUser.get().username());
        assertEquals(email, maybeAuthUser.get().email());
        assertEquals(role, maybeAuthUser.get().role());
    }

    @Test
    void getGet() {
        UUID id = UUID.randomUUID();
        Optional<User> maybeUser = mySQLAuthRepository.findById(id);
        assertTrue(maybeUser.isEmpty());
    }
}