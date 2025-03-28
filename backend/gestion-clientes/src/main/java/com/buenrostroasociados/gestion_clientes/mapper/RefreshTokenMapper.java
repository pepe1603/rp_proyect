package com.buenrostroasociados.gestion_clientes.mapper;

import com.buenrostroasociados.gestion_clientes.dto.auth.RefreshTokenDTO;
import com.buenrostroasociados.gestion_clientes.entity.auth.RefreshToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {

    RefreshTokenDTO toDTO (RefreshToken refreshToken);

    RefreshToken toEntity (RefreshTokenDTO refreshTokenDTO);

}
