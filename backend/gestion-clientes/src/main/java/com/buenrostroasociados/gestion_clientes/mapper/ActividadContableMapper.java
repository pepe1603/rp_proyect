package com.buenrostroasociados.gestion_clientes.mapper;

import com.buenrostroasociados.gestion_clientes.dto.ActividadContableDTO;
import com.buenrostroasociados.gestion_clientes.entity.ActividadContable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActividadContableMapper {
    @Mapping(source = "cliente.id", target = "clienteId")
    ActividadContableDTO toDTO(ActividadContable actividadContable);

    @Mapping(source = "clienteId", target = "cliente.id")
    ActividadContable toEntity(ActividadContableDTO actividadContableDTO);
}
