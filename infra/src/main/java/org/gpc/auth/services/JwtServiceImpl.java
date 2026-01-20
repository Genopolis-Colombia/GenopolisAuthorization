package org.gpc.auth.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.gpc.auth.configuration.JwtProperties;
import org.gpc.auth.error.InvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Map;

public class JwtServiceImpl implements JwtService {
    private final JwtProperties jwtProperties;
    private final Algorithm ALGORITHM;
    private final JWTVerifier verifier;
    private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);


    public JwtServiceImpl(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.ALGORITHM = switch (jwtProperties.algorithm()) {
            case "HMAC256" -> Algorithm.HMAC256(jwtProperties.secret());
            case "HMAC512" -> Algorithm.HMAC512(jwtProperties.secret());
            default -> throw new IllegalArgumentException("Unsupported algorithm: " + jwtProperties.algorithm());
        };
        JWTVerifier.BaseVerification verification = (JWTVerifier.BaseVerification) JWT.require(ALGORITHM);

        if (jwtProperties.issuer() != null) {
            verification.withIssuer(jwtProperties.issuer());
        }
        if (jwtProperties.audience() != null) {
            verification.withAudience(jwtProperties.audience());
        }

        this.verifier = verification.build();
    }

    @Override
    public String generateToken(String subject, Map<String, Object> claims) {
        return generateToken(
                subject,
                jwtProperties.issuer(),
                jwtProperties.audience(),
                jwtProperties.accessTokenExpirationSeconds(),
                claims
        );
    }

    @Override
    public String generateToken(String subject, String issuer, String audience, long expiresInSec, Map<String, Object> claims) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(expiresInSec);

        var builder = JWT.create()
                .withSubject(subject)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt);

        if (issuer != null) {
            builder.withIssuer(issuer);
        }
        if (audience != null) {
            builder.withAudience(audience);
        }
        if (claims != null) {
            claims.forEach((key, value) -> {
                if (value instanceof String str) {
                    builder.withClaim(key, str);
                } else if (value instanceof Integer i) {
                    builder.withClaim(key, i);
                } else if (value instanceof Long l) {
                    builder.withClaim(key, l);
                } else if (value instanceof Boolean b) {
                    builder.withClaim(key, b);
                } else if (value instanceof String[] array) {
                    builder.withArrayClaim(key, array);
                } else {
                    builder.withClaim(key, value.toString());
                }
            });
        }

        return builder.sign(ALGORITHM);
    }

    @Override
    public Map<String, Object> customClaims(String username, String role) {
        return Map.of(
                "username", username,
                "role", role
        );
    }

    @Override
    public String verify(String token) throws InvalidTokenException {
        try {
            return verifier.verify(token).getSubject();
        } catch (TokenExpiredException ex) {
            logger.error("Token expired");
            throw new InvalidTokenException("The provided token has expired");
        } catch (SignatureVerificationException ex) {
            logger.error("Invalid token signature");
            throw new InvalidTokenException("The provided token has an invalid signature");
        } catch (JWTVerificationException e) {
            logger.error("Token invalid");
            throw new InvalidTokenException("The provided token is not valid");
        }
    }
}
