package com.buenrostroasociados.gestion_clientes.service.jwtRefreshToken;

import com.buenrostroasociados.gestion_clientes.entity.Usuario;
import com.buenrostroasociados.gestion_clientes.entity.auth.RefreshToken;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.repository.auth.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{

    @Autowired
    private RefreshTokenRepository refreshTokenRepo;

    @Override
    public RefreshToken generateRefreshToken(Usuario usuario) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7)); // Ajusta la duracion a 7 dias
        refreshToken.setUser(usuario);//asociamos el token al usuario
        return refreshTokenRepo.save(refreshToken);
    }

    @Override
    public RefreshToken findByToken(String token) {

        return refreshTokenRepo.findByToken(token).orElseThrow(
                () -> new EntityNotFoundException("Token-refresh not found: ")
        );

    }

    @Override
    public void deleteByToken(String token) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepo.findByToken(token);
        if (refreshTokenOptional.isPresent()) {
            refreshTokenRepo.delete(refreshTokenOptional.get());
        }
    }
}
