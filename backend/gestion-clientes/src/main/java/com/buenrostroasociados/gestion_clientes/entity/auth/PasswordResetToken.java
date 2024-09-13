package com.buenrostroasociados.gestion_clientes.entity.auth;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;
    @Column String email;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

}
