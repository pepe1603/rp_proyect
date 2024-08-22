package com.buenrostroasociados.gestion_clientes.service;

import com.buenrostroasociados.gestion_clientes.dto.UsuarioDTO;

import java.util.List;
import java.util.Map;

public interface UsuarioService {
    UsuarioDTO saveUsuario(UsuarioDTO usuarioDTO);
    UsuarioDTO getUsuarioById(Long id);
    List<UsuarioDTO> getAllUsuarios();
    UsuarioDTO updateUsuario(Long id, UsuarioDTO usuarioDTO);
    UsuarioDTO updateUsuario(Long id, Map<String, Object> updates);
    void deleteUsuario(Long id);
}
