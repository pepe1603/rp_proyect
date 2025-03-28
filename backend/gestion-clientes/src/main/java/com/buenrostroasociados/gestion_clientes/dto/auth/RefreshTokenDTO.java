package com.buenrostroasociados.gestion_clientes.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDTO {
    private Long id;
    private String token;
    private LocalDateTime expiryDate;

}
