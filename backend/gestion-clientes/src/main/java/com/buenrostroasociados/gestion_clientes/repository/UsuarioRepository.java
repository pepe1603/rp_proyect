package com.buenrostroasociados.gestion_clientes.repository;

import com.buenrostroasociados.gestion_clientes.entity.Rol;
import com.buenrostroasociados.gestion_clientes.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername( String username);
    List<Usuario> findByRoles(Rol role);//obtener todos los ususarios con rol
}
