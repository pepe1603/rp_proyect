package com.buenrostroasociados.gestion_clientes.mapper;

import com.buenrostroasociados.gestion_clientes.dto.ArchivoDTO;
import com.buenrostroasociados.gestion_clientes.entity.Archivo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArchivoMapper {
    @Mapping(source = "actividadContable.id", target = "actividadContableId")
    @Mapping(source = "actividadLitigio.id", target = "actividadLitigioId")
    ArchivoDTO toDTO(Archivo archivo);

    @Mapping(source = "actividadContableId", target = "actividadContable.id")
    @Mapping(source = "actividadLitigioId", target = "actividadLitigio.id")
    Archivo toEntity(ArchivoDTO archivoDTO);
}
