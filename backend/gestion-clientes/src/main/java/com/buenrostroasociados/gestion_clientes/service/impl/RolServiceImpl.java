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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private RolMapper rolMapper;

    @Override
    public RolDTO createRol(RolDTO rolDTO) {
        validateRol(NombreRol.valueOf(rolDTO.getNombre().toUpperCase()));
        rolDTO.getNombre().toUpperCase();// Validar que el rol existe en el enum
        Rol rol = rolMapper.toEntity(rolDTO);
        Rol savedRol = rolRepository.save(rol);
        return rolMapper.toDTO(savedRol);
    }

    @Override
    public RolDTO getRolById(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
        return rolMapper.toDTO(rol);
    }

    @Override
    public List<RolDTO> getAllRoles() {
        return rolRepository.findAll().stream()
                .map(rolMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RolDTO updateRol(Long id, RolDTO rolDTO) {
        validateRol(NombreRol.valueOf(rolDTO.getNombre().toUpperCase())); // Validar que el rol existe en el enum
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));

        rol.setNombre(NombreRol.valueOf(rolDTO.getNombre().toUpperCase()));
        Rol updatedRol = rolRepository.save(rol);
        return rolMapper.toDTO(updatedRol);
    }

    @Override
    public void deleteRol(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));

        rolRepository.delete(rol);
    }

    private void validateRol(NombreRol nombreRol) {
        // Validar si el rol está en el enum NombreRol
        if (nombreRol == null || !Set.of(NombreRol.values()).contains(nombreRol)) {
            throw new IllegalArgumentException("Rol no válido: " + nombreRol);
        }
    }
}
