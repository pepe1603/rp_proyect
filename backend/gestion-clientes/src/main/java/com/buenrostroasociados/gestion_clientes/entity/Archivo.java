package com.buenrostroasociados.gestion_clientes.entity;

import com.buenrostroasociados.gestion_clientes.enums.ClaseArchivo;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaseArchivo tipoArchivo; // Definición del tipo de archivo (LITGIO O CONTABLE)

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
    @ManyToOne
    @JoinColumn(name = "actividad_contable_id")
    private ActividadContable actividadContable;

    @ManyToOne
    @JoinColumn(name = "actividad_litigio_id")
    private ActividadLitigio actividadLitigio;
}