package org.gpc.auth.services;


import org.gpc.auth.error.InvalidTokenException;

import java.util.Map;

public interface JwtService {

    String generateToken(String subject, Map<String, Object> claims);

    String generateToken(String subject,
                                String issuer,
                                String audience,
                                long expiresInSec,
                                Map<String, Object> claims);

    Map<String, Object> customClaims(String username, String role);

    String verify(String token) throws InvalidTokenException;
}
