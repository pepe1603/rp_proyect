package com.buenrostroasociados.gestion_clientes.mapper;

import com.buenrostroasociados.gestion_clientes.dto.ActividadLitigioDTO;
import com.buenrostroasociados.gestion_clientes.entity.ActividadLitigio;
import com.buenrostroasociados.gestion_clientes.enums.EstadoCaso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ActividadLitigioMapper {

    @Mapping(source = "estadoCaso", target = "estadoCaso", qualifiedByName = "stringToEstadoCaso")
    @Mapping(source = "cliente.id", target = "clienteId")
    ActividadLitigioDTO toDTO(ActividadLitigio actividadLitigio);

    @Mapping(source = "estadoCaso", target = "estadoCaso", qualifiedByName = "estadoCasoToString")
    @Mapping(source = "clienteId", target = "cliente.id")
    ActividadLitigio toEntity(ActividadLitigioDTO actividadLitigioDTO);

    @Named("stringToEstadoCaso")
    default EstadoCaso stringToEstadoCaso(String estadoCaso) {
        return estadoCaso != null ? EstadoCaso.valueOf(estadoCaso) : null;
    }

    @Named("estadoCasoToString")
    default String estadoCasoToString(EstadoCaso estadoCaso) {
        return estadoCaso != null ? estadoCaso.name() : null;
    }
}