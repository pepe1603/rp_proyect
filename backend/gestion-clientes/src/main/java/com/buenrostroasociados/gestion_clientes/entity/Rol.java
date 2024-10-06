package com.buenrostroasociados.gestion_clientes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol implements GrantedAuthority {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)///camiar por String no por enum paa evitar manejos complejos de guardados en usuario y getautorities y authservice y repsitorio demas que denpendan de este campo
    private String nombre;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.nombre;
    }




}
