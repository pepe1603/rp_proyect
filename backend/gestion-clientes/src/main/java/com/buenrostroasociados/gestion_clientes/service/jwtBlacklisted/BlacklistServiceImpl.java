package com.buenrostroasociados.gestion_clientes.service.jwtBlacklisted;

import com.buenrostroasociados.gestion_clientes.entity.auth.BlacklistedToken;
import com.buenrostroasociados.gestion_clientes.repository.auth.BlacklistedTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class BlacklistServiceImpl implements BlacklistedService {
    private static final Logger logger = LoggerFactory.getLogger(BlacklistServiceImpl.class);
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
    @Transactional
    @Scheduled(fixedRate = 3600000 )// por el moemnto es cada 1hora y si queremos en 10 min = 600000,)
    public void cleanupExpiredTokens() {
        logger.info("Verifying Blacklist...");
        blacklistedTokenRepo.deleteByExpiresAtBefore(Instant.now());
        logger.info("Expired tokens cleaned up from the blacklist.");

    }
}
