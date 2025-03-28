package com.buenrostroasociados.gestion_clientes.service.jwtRefreshToken;

import com.buenrostroasociados.gestion_clientes.dto.auth.RefreshTokenDTO;
import com.buenrostroasociados.gestion_clientes.entity.Usuario;

import java.util.List;

public interface RefreshTokenService {

    RefreshTokenDTO generateRefreshToken(Usuario usuario);

    RefreshTokenDTO findByToken(String token);

    RefreshTokenDTO findByUser(Usuario user);

    List<RefreshTokenDTO> findAllByUser(Usuario user);

    void deleteAllByUser(Usuario usuario);

    void deleteByToken(String token);
}
