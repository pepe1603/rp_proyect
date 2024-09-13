package com.buenrostroasociados.gestion_clientes.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SigninResponse {
    private String token;
    private String refreshToken;
    private String rolename;
}
