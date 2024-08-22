package com.buenrostroasociados.gestion_clientes.service.impl;

import com.buenrostroasociados.gestion_clientes.dto.RolDTO;
import com.buenrostroasociados.gestion_clientes.entity.Rol;
import com.buenrostroasociados.gestion_clientes.enums.NombreRol;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.mapper.RolMapper;
import com.buenrostroasociados.gestion_clientes.repository.RolRepository;
import com.buenrostroasociados.gestion_clientes.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolServiceImpl implements RolService {
    @Autowired
    private RolRepository rolRepo;
    @Autowired
    private RolMapper rolMapper;


    @Override
    public RolDTO saveRol(RolDTO rolDTO) {
        Rol newRol = rolMapper.toEntity(rolDTO);

        Rol savedRol = rolRepo.save(newRol);
        return rolMapper.toDTO(savedRol);
    }

    @Override
    public RolDTO getRolById(Long id) {
        Rol rolfounded= rolRepo.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Rol no encontrado con ID: "+id)
                );
        return rolMapper.toDTO(rolfounded);
    }

    @Override
    public List<RolDTO> getAllRoles() {
        List<Rol> roles = rolRepo.findAll() ;
        if (roles.isEmpty()){
            throw new EntityNotFoundException("No se encontraron Roles en el repositorio.");
        };
        return roles.stream()
                .map(rolMapper::toDTO)
                .collect(Collectors.toList()
                );
    }

    @Override
    public RolDTO updateRol(Long id, RolDTO rolDTO) {
        Rol rolfounded= rolRepo.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Rol no encontrado con ID: "+id)
                );
        NombreRol rol = NombreRol.valueOf(rolDTO.getNombre().toUpperCase());
        rolfounded.setNombre( rol );

        Rol savedRol = rolRepo.save(rolfounded);
        return rolMapper.toDTO(savedRol);
    }

    @Override
    public void deleteRol(Long id) {
        Rol rolfounded= rolRepo.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("El rol no fue elimnado por que no fue encontrado con ID: "+id)
                );
    }

}
