package org.gpc.auth.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.jwt")
public record JwtProperties(
        String secret,
        String issuer,
        String audience,
        String algorithm,
        long accessTokenExpirationSeconds,
        long refreshTokenExpirationSeconds
) { }