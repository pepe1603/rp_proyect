package com.buenrostroasociados.gestion_clientes.mapper;

import com.buenrostroasociados.gestion_clientes.dto.ActividadContableDTO;
import com.buenrostroasociados.gestion_clientes.entity.ActividadContable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ActividadContableMapper {
    @Mapping(source = "cliente.id", target = "clienteId")
    ActividadContableDTO toDTO(ActividadContable actividadContable);

    @Mapping(source = "clienteId", target = "cliente.id")
    ActividadContable toEntity(ActividadContableDTO actividadContableDTO);

    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "id", ignore = true)// Evita la actualización del cliente si no es necesario
    void updateEntity(ActividadContableDTO dto, @MappingTarget ActividadContable entity);

}
