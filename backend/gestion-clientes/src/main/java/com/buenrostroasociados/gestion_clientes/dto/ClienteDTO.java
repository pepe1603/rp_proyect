package com.buenrostroasociados.gestion_clientes.dto;

import lombok.Data;

@Data
public class ClienteDTO {
    private Long id;
    private String rfc;
    private String nombre;
    private String apellidos;
    private String correo;
    private String telefono;
}
