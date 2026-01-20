package org.gpc.auth.services;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BCryptPasswordHasherService implements PasswordHasherService {

    private final PasswordEncoder passwordEncoder;

    public BCryptPasswordHasherService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public BCryptPasswordHasherService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String hash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String passwordHash) {
        return passwordEncoder.matches(rawPassword, passwordHash);
    }
}
