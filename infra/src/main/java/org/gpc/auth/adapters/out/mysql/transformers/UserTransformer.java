package org.gpc.auth.adapters.out.mysql.transformers;

import org.gpc.auth.adapters.out.mysql.model.UserEntity;
import org.gpc.auth.adapters.out.mysql.model.UserEntity.RoleEntity;
import org.gpc.auth.kernel.Role;
import org.gpc.auth.kernel.User;
import org.gpc.auth.kernel.UserRegistration;
import org.gpc.auth.kernel.UserUpdate;

public class UserTransformer {

    private UserTransformer() {}

    public static UserEntity registrationToEntity(UserRegistration registration, String passwordHash) {
        UserEntity entity = new UserEntity();
        entity.setUsername(registration.username());
        entity.setEmail(registration.email());
        entity.setPasswordHash(passwordHash);
        entity.setRole(RoleEntity.valueOf(registration.role().name()));
        return entity;
    }

    public static User entityToDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPasswordHash(),
                Role.valueOf(entity.getRole().name()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static UserEntity updateToEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.id());
        entity.setUsername(user.username());
        entity.setEmail(user.email());
        entity.setPasswordHash(user.passwordHash());
        entity.setRole(RoleEntity.valueOf(user.role().name()));
        entity.setCreatedAt(user.createdAt());
        entity.setUpdatedAt(user.updatedAt());
        return entity;
    }
}