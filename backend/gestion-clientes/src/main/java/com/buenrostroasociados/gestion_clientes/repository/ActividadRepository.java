package com.buenrostroasociados.gestion_clientes.repository;

import com.buenrostroasociados.gestion_clientes.entity.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

}
