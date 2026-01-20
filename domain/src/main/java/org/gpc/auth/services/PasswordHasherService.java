package org.gpc.auth.services;

public interface PasswordHasherService {
    String hash(String rawPassword);

    boolean matches(String rawPassword, String passwordHash);
}
