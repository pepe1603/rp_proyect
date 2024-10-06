package com.buenrostroasociados.gestion_clientes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    @NotBlank(message = "El Username no debe estar en blanco")
    private String username;
    @NotBlank(message = "El passwprd no debe estar5 en blanco")
    private String password;
    @Email
    private String email;
    private Set<RolDTO> roles;  // Los roles pueden ser simples, como una lista de nombres de roles.
}
