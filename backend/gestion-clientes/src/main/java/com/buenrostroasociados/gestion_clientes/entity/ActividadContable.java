package com.buenrostroasociados.gestion_clientes.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "actividades_contables")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActividadContable extends Actividad {

    @OneToMany(mappedBy = "actividadContable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Archivo> archivos;

    @Column(nullable = false)
    private String tipoDocumento; // Opinión de cumplimiento, pagos provisionales, etc.

}