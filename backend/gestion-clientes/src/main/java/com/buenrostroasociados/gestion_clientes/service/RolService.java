package com.buenrostroasociados.gestion_clientes.service;

import com.buenrostroasociados.gestion_clientes.dto.RolDTO;

import java.util.List;

public interface RolService {

    RolDTO createRol(RolDTO rolDTO);

    RolDTO getRolById(Long id);

    List<RolDTO> getAllRoles();

    RolDTO updateRol(Long id, RolDTO rolDTO);

    void deleteRol(Long id);
}
