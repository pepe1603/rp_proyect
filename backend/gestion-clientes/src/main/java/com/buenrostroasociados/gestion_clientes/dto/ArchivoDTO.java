package com.buenrostroasociados.gestion_clientes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArchivoDTO {
    private Long id;
    @NotBlank(message = "el nombre del archivo no puede estar vacio")
    private String nombreArchivo;
    @NotBlank(message = "La ruta del archivo no  puede estar vacia")
    private String rutaArchivo;
    @NotBlank(message = "Debe de ingresar el Tipo de Archivo")
    private String tipoArchivo; // Definición del tipo de archivo
    private LocalDateTime fechaCreacion;
    private Long actividadContableId;  // Referencia a la actividad contable, si aplica
    private Long actividadLitigioId;  // Referencia a la actividad de litigio, si aplica

}

