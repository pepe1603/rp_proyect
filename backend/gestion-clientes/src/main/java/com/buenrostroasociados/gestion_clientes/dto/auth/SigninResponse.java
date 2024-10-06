package com.buenrostroasociados.gestion_clientes.dto.auth;

import com.buenrostroasociados.gestion_clientes.dto.UsuarioDTO;
import com.buenrostroasociados.gestion_clientes.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SigninResponse {
    private String token;
    private String refreshToken;
    private UsuarioDTO userData;
}
