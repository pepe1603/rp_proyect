package com.buenrostroasociados.gestion_clientes.mapper;

import com.buenrostroasociados.gestion_clientes.dto.ActividadLitigioDTO;
import com.buenrostroasociados.gestion_clientes.entity.ActividadLitigio;
import com.buenrostroasociados.gestion_clientes.enums.EstadoCaso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ActividadLitigioMapper {

    @Mapping(source = "estadoCaso", target = "estadoCaso", qualifiedByName = "mapEstadoCasoToString")
    @Mapping(source = "cliente.id", target = "clienteId")
    ActividadLitigioDTO toDTO(ActividadLitigio actividadLitigio);

    @Mapping(source = "estadoCaso", target = "estadoCaso", qualifiedByName = "mapStringToEstadoCaso")
    @Mapping(source = "clienteId", target = "cliente.id")
    ActividadLitigio toEntity(ActividadLitigioDTO actividadLitigioDTO);

    @Named("mapStringToEstadoCaso")
    default EstadoCaso mapStringToEstadoCaso(String estadoCaso) {
        return estadoCaso != null ? EstadoCaso.valueOf(estadoCaso) : null;
    }

    @Named("mapEstadoCasoToString")
    default String mapEstadoCasoToString(EstadoCaso estadoCaso) {
        return estadoCaso != null ? estadoCaso.name() : null;
    }
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "id", ignore = true)// Evita la actualización del cliente si no es necesario
    void updateEntity(ActividadLitigioDTO dto, @MappingTarget ActividadLitigio entity);

}
