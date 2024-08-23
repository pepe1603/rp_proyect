package com.buenrostroasociados.gestion_clientes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "archivos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Archivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreArchivo;
    @Column(nullable = false)
    private String rutaArchivo;
    @Column(nullable = false)
    private String tipoArchivo; // Definición del tipo de archivo
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
    @ManyToOne
    @JoinColumn(name = "actividad_contable_id")
    private ActividadContable actividadContable;

    @ManyToOne
    @JoinColumn(name = "actividad_litigio_id")
    private ActividadLitigio actividadLitigio;
}