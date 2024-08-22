package com.buenrostroasociados.gestion_clientes.service.jwtBlacklisted;

import com.buenrostroasociados.gestion_clientes.entity.BlacklistedToken;
import com.buenrostroasociados.gestion_clientes.repository.BlacklistedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class BlacklistServiceImpl implements BlacklistedService {
    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepo;

    @Override
    public void blacklistToken(String token, Instant expiresAt) { // Añade un token a la lista negra con su fecha de expiración
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setBlacklistedAt(Instant.now());
        blacklistedToken.setExpiresAt(expiresAt);
        blacklistedTokenRepo.save(blacklistedToken);
    }

    @Override
    public boolean isTokenBlacklisted(String token) { // Verifica si un token está en la lista negra
        BlacklistedToken blacklistedToken = blacklistedTokenRepo.findByToken(token).orElse(null);
        return blacklistedToken != null && blacklistedToken.getExpiresAt().isAfter(Instant.now());
    }

    @Override
    @Scheduled(fixedRate =  600000)// Cada hora3600000)
    public void cleanupExpiredTokens() {
        blacklistedTokenRepo.deleteByExpiresAtBefore(Instant.now());
    }
}
