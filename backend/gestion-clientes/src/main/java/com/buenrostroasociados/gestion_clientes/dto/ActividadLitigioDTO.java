package com.buenrostroasociados.gestion_clientes.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActividadLitigioDTO {
    private Long id;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private Long clienteId;  // Referencia al cliente asociado
    private String estadoCaso;  // Estado del caso, seguimiento o avance
    private List<ArchivoDTO> documentos;  // Lista de documentos asociados
}
