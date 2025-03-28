package com.buenrostroasociados.gestion_clientes.mapper;

import com.buenrostroasociados.gestion_clientes.dto.UsuarioDTO;
import com.buenrostroasociados.gestion_clientes.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {RolMapper.class})
public interface UsuarioMapper {

    UsuarioDTO toDTO(Usuario usuario);

    Usuario toEntity(UsuarioDTO usuarioDTO);
}
