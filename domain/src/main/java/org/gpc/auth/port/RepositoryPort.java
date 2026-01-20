package org.gpc.auth.port;

import org.gpc.auth.kernel.User;
import org.gpc.auth.kernel.UserRegistration;
import java.util.Optional;
import java.util.UUID;

public interface RepositoryPort {

    Optional<User> updateUser(User user);

    void deleteUser(UUID id);

    UUID saveUser(UserRegistration userRegistration, String passwordHash);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findById(UUID id);

    void deleteAll();
}