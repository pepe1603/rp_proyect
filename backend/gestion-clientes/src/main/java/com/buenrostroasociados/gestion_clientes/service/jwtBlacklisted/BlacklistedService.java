package com.buenrostroasociados.gestion_clientes.service.jwtBlacklisted;

import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;

public interface BlacklistedService {

    void blacklistToken(String token, Instant expiresAt);

    boolean isTokenBlacklisted(String token);

    @Scheduled(fixedRate = 3600000) // Cada hora
    void cleanupExpiredTokens();
}