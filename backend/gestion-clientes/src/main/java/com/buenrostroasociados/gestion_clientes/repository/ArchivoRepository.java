package com.buenrostroasociados.gestion_clientes.repository;

import com.buenrostroasociados.gestion_clientes.entity.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, Long> {

    Optional<Archivo> findByNombreArchivo(String nombreArchivo);

    @Query(value = "SELECT * FROM archivos WHERE actividad_contable_id = :id", nativeQuery = true)
    List<Archivo> findArchivosByActividadContableId(@Param("id") Long id);

    @Query(value = "SELECT * FROM archivos WHERE actividad_litigio_id = :id", nativeQuery = true)
    List<Archivo> findArchivosByActividadLitigioId(@Param("id") Long id);
}
