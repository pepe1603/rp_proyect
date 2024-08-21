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
@Table(name = "actividades_litigio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActividadLitigio extends Actividad {

    private String estadoCaso; // Seguimiento del proceso o avance del caso

    @OneToMany(mappedBy = "actividadLitigio", cascade = CascadeType.ALL)
    private List<Archivo> documentos;
}