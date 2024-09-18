package com.buenrostroasociados.gestion_clientes.service;

import com.buenrostroasociados.gestion_clientes.dto.UsuarioDTO;
import com.buenrostroasociados.gestion_clientes.dto.auth.SignupRequest;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;

public interface UsuarioService {
    UsuarioDTO saveUsuario(SignupRequest usuarioDTO);
    UsuarioDTO getUsuarioById(Long id);
    List<UsuarioDTO> getAllUsuarios();
    UsuarioDTO updateUsuario(Long id, UsuarioDTO usuarioDTO);

    UsuarioDTO updateUsername(Long userId, String newUsername);

    UsuarioDTO updatePassword(Long userId, String newPassword);

    void deleteUsuario(Long id);

    List<String> getAllEmails();

    List<String> getAllAdminEmails();

    Resource exportUsuariosToCSV();

    Resource exportUsuariosToPDF();
}
