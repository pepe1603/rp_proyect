package com.buenrostroasociados.gestion_clientes.repository;

import com.buenrostroasociados.gestion_clientes.entity.ActividadContable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadContableRepository extends JpaRepository<ActividadContable, Long> {
}
