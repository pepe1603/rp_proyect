package com.buenrostroasociados.gestion_clientes.mapper;

import com.buenrostroasociados.gestion_clientes.dto.AdministradorDTO;
import com.buenrostroasociados.gestion_clientes.entity.Administrador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdministradorMapper {
    @Mapping(source = "usuario.id", target = "usuarioId")
    AdministradorDTO toDTO(Administrador administrador);

    @Mapping(source = "usuarioId", target = "usuario.id")
    Administrador toEntity(AdministradorDTO administradorDTO);

}
