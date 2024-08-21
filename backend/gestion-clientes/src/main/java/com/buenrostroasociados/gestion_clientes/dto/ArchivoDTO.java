package com.buenrostroasociados.gestion_clientes.dto;

import lombok.Data;

@Data
public class ArchivoDTO {
    private Long id;
    private String nombreArchivo;
    private String rutaArchivo;
    private Long actividadContableId;  // Referencia a la actividad contable, si aplica
    private Long actividadLitigioId;  // Referencia a la actividad de litigio, si aplica

}

