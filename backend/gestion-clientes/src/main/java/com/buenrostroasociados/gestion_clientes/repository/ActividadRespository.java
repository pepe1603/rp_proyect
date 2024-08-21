package com.buenrostroasociados.gestion_clientes.repository;

import com.buenrostroasociados.gestion_clientes.entity.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActividadRespository extends JpaRepository<Actividad, Long> {

}
