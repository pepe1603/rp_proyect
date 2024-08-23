package com.buenrostroasociados.gestion_clientes.entity;


import com.buenrostroasociados.gestion_clientes.enums.EstadoCaso;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCaso estadoCaso; // Estado del caso, seguimiento o avance

}