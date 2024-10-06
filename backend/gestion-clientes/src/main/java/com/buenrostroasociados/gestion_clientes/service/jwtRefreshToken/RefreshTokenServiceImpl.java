package com.buenrostroasociados.gestion_clientes.service.jwtRefreshToken;

import com.buenrostroasociados.gestion_clientes.dto.auth.RefreshTokenDTO;
import com.buenrostroasociados.gestion_clientes.entity.Usuario;
import com.buenrostroasociados.gestion_clientes.entity.auth.RefreshToken;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.mapper.RefreshTokenMapper;
import com.buenrostroasociados.gestion_clientes.repository.auth.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);
    @Value("${jwt.refresh.expiration.ms}")
    private long refreshExpirationMs;
    @Autowired
    private RefreshTokenMapper refreshTokenMapper;

    @Autowired
    private RefreshTokenRepository refreshTokenRepo;

    @Override
    public RefreshTokenDTO generateRefreshToken(Usuario usuario) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(LocalDateTime.now().plus(Duration.ofMillis(refreshExpirationMs))); // Ajusta la duracion a 7 dias
        refreshToken.setUser(usuario);//asociamos el token al usuario
        RefreshToken savedRefreshToken = refreshTokenRepo.save(refreshToken);

        return refreshTokenMapper.toDTO(savedRefreshToken);
    }

    @Override
    public RefreshTokenDTO findByToken(String token) {

        RefreshToken refreshToken= refreshTokenRepo.findByToken(token).orElseThrow(
                () -> new EntityNotFoundException("Token-refresh not found: "+token)
        );
        return refreshTokenMapper.toDTO(refreshToken);

    }
    @Override
    public RefreshTokenDTO findByUser(Usuario user) {
        logger.info("Buscando Ussuario en RefreshTokens..");

        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepo.findByUser(user);
        if (refreshTokenOptional.isPresent()) {
            RefreshToken refreshToken = refreshTokenOptional.get();
            logger.info("RefreshToken Founded : {}", refreshToken.getToken());
            logger.warn("Mapping Token Entity to DTO..");

            return refreshTokenMapper.toDTO(refreshToken);
            //return new RefreshTokenDTO(refreshToken.getToken(), refreshToken.getExpiryDate());


        }
        logger.error("Token rEfresh Not Founded With User : {}", user.getUsername());
        throw new EntityNotFoundException("Toke Refesh Not Founded With User "+user.getUsername()); // O lanza una excepción si prefieres
    }
    @Override
    public List<RefreshTokenDTO> findAllByUser(Usuario user) {
        Optional<RefreshToken> refreshTokens;
        logger.info("Buscando Usuario en RefreshTokens..");
        refreshTokens = refreshTokenRepo.findByUser(user);
        logger.info("RefresthTokens Entroncrados...");

        if (refreshTokens.isEmpty()){
            logger.error("Token rEfresh Not Founded With User : {}", user.getUsername());
            throw new EntityNotFoundException("Toke Refesh Not Founded With User "+user.getUsername()); // O lanza una excepción si prefieres
        }

        return refreshTokens.stream()
                        .map(refreshTokenMapper::toDTO)
                                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllByUser(Usuario usuario) {
        List<RefreshToken> refreshTokens = this.findAllByUser(usuario).stream()
                                                .map(refreshTokenMapper::toEntity)
                                                .toList();
        logger.info("TokensRefresh {} obtenidos.., ", (long) refreshTokens.size());
        for (RefreshToken token: refreshTokens){
            refreshTokenRepo.delete(token);
            logger.info("Refresh Tokens Deleted by id: {}", token.getId().toString());
        }
    }


    @Override
    public void deleteByToken(String token) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepo.findByToken(token);
        if (refreshTokenOptional.isPresent()) {
            logger.info("Deleting refresh token: {}", refreshTokenOptional.get().getToken());
            refreshTokenRepo.delete(refreshTokenOptional.get());
        } else {
            logger.warn("Attempted to delete non-existent refresh token: {}", token);
        }
    }

}
