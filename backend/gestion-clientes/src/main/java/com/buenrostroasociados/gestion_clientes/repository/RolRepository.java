package com.buenrostroasociados.gestion_clientes.repository;

import com.buenrostroasociados.gestion_clientes.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombre(String nombreRol);

    boolean existsByNombre(String nombreRol);
}
