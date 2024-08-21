package com.buenrostroasociados.gestion_clientes.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UsuarioDTO {
    private Long id;
    private String username;
    private String password;
    private Set<RolDTO> roles;  // Los roles pueden ser simples, como una lista de nombres de roles.
}
