package com.buenrostroasociados.gestion_clientes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActividadLitigioDTO {
    private Long id;
    @NotBlank(message = "La deescripcion no puede estar en blanco")
    private String descripcion;
    @NotBlank(message = "La fecha de creacion no puede estar en blanco")
    private LocalDateTime fechaCreacion;
    @NotBlank(message = "El id del Cliente no puede estar en blanco")
    private Long clienteId;  // Referencia al cliente asociado
    @NotBlank(message = "el estado del caso no puede estar en blanco")
    private String estadoCaso;  // Estado del caso, seguimiento o avance
    private List<ArchivoDTO> documentos;  // Lista de documentos asociados
}
