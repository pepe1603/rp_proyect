package com.buenrostroasociados.gestion_clientes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActividadLitigioDTO {
    private Long id;
    @NotBlank(message = "La descripcion no puede estar en blanco")
    private String descripcion;
    @NotNull(message = "La fecha de creacion no puede estar en blanco")
    private LocalDateTime fechaCreacion;
    @NotNull(message = "El id del Cliente no puede estar en blanco")
    private Long clienteId;  // Referencia al cliente asociado
    @NotBlank(message = "el estado del caso no puede estar en blanco")
    private String estadoCaso;  // Estado del caso, seguimiento o avance

}
