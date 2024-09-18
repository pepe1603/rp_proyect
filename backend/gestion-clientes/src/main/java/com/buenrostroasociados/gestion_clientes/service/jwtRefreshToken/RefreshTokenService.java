package com.buenrostroasociados.gestion_clientes.service.jwtRefreshToken;

import com.buenrostroasociados.gestion_clientes.entity.Usuario;
import com.buenrostroasociados.gestion_clientes.entity.auth.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken generateRefreshToken(Usuario usuario);

    RefreshToken findByToken(String token);

    void deleteByToken(String token);
}
