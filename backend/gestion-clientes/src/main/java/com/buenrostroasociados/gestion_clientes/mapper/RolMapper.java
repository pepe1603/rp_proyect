package com.buenrostroasociados.gestion_clientes.mapper;

import com.buenrostroasociados.gestion_clientes.dto.RolDTO;
import com.buenrostroasociados.gestion_clientes.entity.Rol;
import com.buenrostroasociados.gestion_clientes.enums.NombreRol;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RolMapper {

    // Mapea el Rol a RolDTO
    @Mapping(source = "nombre", target = "nombre")
    RolDTO toDTO(Rol rol);

    // Mapea el RolDTO a Rol
    @Mapping(source = "nombre", target = "nombre")
    Rol toEntity(RolDTO rolDTO);

    // Métodos adicionales para manejar el mapeo de NombreRol -> es decir deviolfvere el enum corecto
    default String map(NombreRol nombreRol) {
        return nombreRol != null ? nombreRol.name() : null;
    }

    default NombreRol map(String nombre) {
        return nombre != null ? NombreRol.valueOf(nombre) : null;
    }
}