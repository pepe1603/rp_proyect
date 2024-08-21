package com.buenrostroasociados.gestion_clientes.dto;

import lombok.Data;

@Data
public class AdministradorDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private Long usuarioId; //referenciamos a Usuaio
}
