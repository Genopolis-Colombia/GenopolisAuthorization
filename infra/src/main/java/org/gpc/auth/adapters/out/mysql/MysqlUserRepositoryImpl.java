package org.gpc.auth.adapters.out.mysql;

import org.gpc.auth.adapters.out.mysql.model.UserEntity;
import org.gpc.auth.kernel.User;
import org.gpc.auth.kernel.UserRegistration;
import org.gpc.auth.port.RepositoryPort;
import org.gpc.auth.adapters.out.mysql.transformers.UserTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

public class MysqlUserRepositoryImpl implements RepositoryPort {

    private static final Logger logger = LoggerFactory.getLogger(MysqlUserRepositoryImpl.class);
    private final UserRepository userRepository;

    public MysqlUserRepositoryImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UUID saveUser(UserRegistration userRegistration, String passwordHash) {
        logger.debug("Persisting new user: {}", userRegistration.username());
        UserEntity entity = UserTransformer.registrationToEntity(userRegistration, passwordHash);
        return userRepository.save(entity).getId();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username).map(UserTransformer::entityToDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserTransformer::entityToDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> updateUser(User userUpdate) {
        logger.debug("Updating user {}", userUpdate.id());
        UserEntity entity = UserTransformer.updateToEntity(userUpdate);
        return Optional.of(userRepository.save(entity))
                .map(UserTransformer::entityToDomain);
    }

    @Override
    public void deleteUser(UUID id) {
        logger.debug("Deleting user {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id).map(UserTransformer::entityToDomain);
    }

    // Solo para testing
    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }
}