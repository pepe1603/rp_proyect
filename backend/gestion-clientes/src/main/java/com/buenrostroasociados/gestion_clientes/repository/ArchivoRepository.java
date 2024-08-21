package com.buenrostroasociados.gestion_clientes.repository;

import com.buenrostroasociados.gestion_clientes.entity.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArchivoRepository extends JpaRepository<Archivo, Long> {

    Optional<Archivo> findByNombreArchivo(String nombreArchivo);
}
