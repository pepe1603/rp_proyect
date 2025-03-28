package com.buenrostroasociados.gestion_clientes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class PublicInfoDTO {
    private String nombre;
    private String descripcion;
    private String version;
    private String contacto;
    private String whatsappLink;
    private String facebookLink;
    private String instagramLink;
    private String servicios;
    private LocalDate releaseDate;  // Nueva propiedad: Fecha de lanzamiento
}
