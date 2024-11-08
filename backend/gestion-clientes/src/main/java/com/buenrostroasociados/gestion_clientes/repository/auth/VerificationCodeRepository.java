package com.buenrostroasociados.gestion_clientes.repository.auth;

import com.buenrostroasociados.gestion_clientes.entity.auth.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByToken(String token);
}
