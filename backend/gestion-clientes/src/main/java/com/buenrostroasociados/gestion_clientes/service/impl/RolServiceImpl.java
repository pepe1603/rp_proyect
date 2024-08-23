package com.buenrostroasociados.gestion_clientes.service.impl;

import com.buenrostroasociados.gestion_clientes.dto.RolDTO;
import com.buenrostroasociados.gestion_clientes.entity.Rol;
import com.buenrostroasociados.gestion_clientes.enums.NombreRol;
import com.buenrostroasociados.gestion_clientes.exception.ResourceNotFoundException;
import com.buenrostroasociados.gestion_clientes.mapper.RolMapper;
import com.buenrostroasociados.gestion_clientes.repository.RolRepository;
import com.buenrostroasociados.gestion_clientes.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private RolMapper rolMapper;

    @Transactional
    @Override
    public RolDTO createRol(RolDTO rolDTO) {
        // Convertir nombre del rol a mayúsculas
        String rolNombreUpper = rolDTO.getNombre().toUpperCase();

        // Validar que el rol existe en el enum
        if (!isValidRolName(rolNombreUpper)) {
            throw new IllegalArgumentException("El rol proporcionado no es válido.");
        }

        // Verificar si el rol ya existe en la base de datos
        if (rolRepository.existsByNombre(NombreRol.valueOf(rolNombreUpper))) {
            throw new IllegalArgumentException("El rol ya existe.");
        }

        // Convertir DTO a entidad
        Rol rol = rolMapper.toEntity(rolDTO);
        rol.setNombre(NombreRol.valueOf(rolNombreUpper)); // Asegurar que el nombre está en mayúsculas

        // Guardar la entidad en la base de datos
        Rol savedRol = rolRepository.save(rol);

        // Convertir la entidad guardada de nuevo a DTO y devolverlo
        return rolMapper.toDTO(savedRol);
    }

    @Override
    public RolDTO getRolById(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
        return rolMapper.toDTO(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDTO> getAllRoles() {
        return rolRepository.findAll().stream()
                .map(rolMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public RolDTO updateRol(Long id, RolDTO rolDTO) {
        // Validar que el rol existe en el enum
        String rolNombreUpper = rolDTO.getNombre().toUpperCase();
        if (!isValidRolName(rolNombreUpper)) {
            throw new IllegalArgumentException("El rol proporcionado no es válido.");
        }

        // Buscar el rol existente
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));

        // Actualizar el rol
        rol.setNombre(NombreRol.valueOf(rolNombreUpper));
        Rol updatedRol = rolRepository.save(rol);
        return rolMapper.toDTO(updatedRol);
    }

    @Transactional
    @Override
    public void deleteRol(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));

        rolRepository.delete(rol);
    }

    private boolean isValidRolName(String roleName) {
        try {
            NombreRol.valueOf(roleName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
