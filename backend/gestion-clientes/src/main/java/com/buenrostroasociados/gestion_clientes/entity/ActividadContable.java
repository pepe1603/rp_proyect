package com.buenrostroasociados.gestion_clientes.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    @OneToMany(mappedBy = "actividadContable", cascade = CascadeType.ALL)
    private List<Archivo> archivos;

    private String tipoDocumento; // Opinión de cumplimiento, pagos provisionales, etc.
}