package com.buenrostroasociados.gestion_clientes.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActividadContableDTO {
    private Long id;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private Long clienteId;  // Referencia al cliente asociado
    private String tipoDocumento;  // Opinión de cumplimiento, pagos provisionales, etc.
    private List<ArchivoDTO> archivos;  // Lista de archivos asociados
}
