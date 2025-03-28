package com.buenrostroasociados.gestion_clientes.repository;

import com.buenrostroasociados.gestion_clientes.entity.ActividadLitigio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadLitigioRepository extends JpaRepository<ActividadLitigio, Long> {
}
