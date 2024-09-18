package com.buenrostroasociados.gestion_clientes.mapper;

import com.buenrostroasociados.gestion_clientes.dto.ActividadLitigioDTO;
import com.buenrostroasociados.gestion_clientes.dto.ArchivoDTO;
import com.buenrostroasociados.gestion_clientes.entity.ActividadLitigio;
import com.buenrostroasociados.gestion_clientes.entity.Archivo;
import com.buenrostroasociados.gestion_clientes.enums.ClaseArchivo;
import com.buenrostroasociados.gestion_clientes.enums.EstadoCaso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ArchivoMapper {
    @Mapping(source = "tipoArchivo", target = "tipoArchivo", qualifiedByName = "mapClaseArchivoToString")
    @Mapping(source = "actividadContable.id", target = "actividadContableId")
    @Mapping(source = "actividadLitigio.id", target = "actividadLitigioId")
    ArchivoDTO toDTO(Archivo archivo);

    @Mapping(source = "tipoArchivo", target = "tipoArchivo", qualifiedByName = "mapStringToClaseArchivo")
    @Mapping(source = "actividadContableId", target = "actividadContable.id")
    @Mapping(source = "actividadLitigioId", target = "actividadLitigio.id")
    Archivo toEntity(ArchivoDTO archivoDTO);

    @Named("mapStringToClaseArchivo")
    default ClaseArchivo mapStringToClaseArchivo(String tipoArchivo) {
        return tipoArchivo != null ? ClaseArchivo.valueOf(tipoArchivo) : null;
    }

    @Named("mapClaseArchivoToString")
    default String mapClaseArchivoToString(ClaseArchivo claseArchivo) {
        return claseArchivo != null ? claseArchivo.name() : null;
    }

    void updateEntity(ActividadLitigioDTO dto, @MappingTarget ActividadLitigio entity);


}
