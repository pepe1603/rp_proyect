package com.buenrostroasociados.gestion_clientes.repository.auth;

import com.buenrostroasociados.gestion_clientes.entity.auth.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface BlacklistedTokenRepository  extends JpaRepository<BlacklistedToken, Long> {
    Optional<BlacklistedToken> findByToken(String token);

    void deleteByExpiresAtBefore(Instant now);
}