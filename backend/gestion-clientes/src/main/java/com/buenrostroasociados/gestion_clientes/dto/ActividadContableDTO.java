package com.buenrostroasociados.gestion_clientes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActividadContableDTO {
    private Long id;
    @NotBlank(message = "El titulo no puede estar vacio")
    private String titulo;
    @NotBlank(message = "La descripcion no puede estar en blanco")
    private String descripcion;
    @NotBlank(message = "La fecha de creacion no puede estar en blanco")
    private LocalDateTime fechaCreacion;
    @NotBlank(message = "EL id dedl cliente no puede estar en blanco")
    private Long clienteId;  // Referencia al cliente asociado

}

