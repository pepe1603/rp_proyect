package com.buenrostroasociados.gestion_clientes.service.impl;

import com.buenrostroasociados.gestion_clientes.dto.RolDTO;
import com.buenrostroasociados.gestion_clientes.entity.Rol;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.exception.ResourceNotFoundException;
import com.buenrostroasociados.gestion_clientes.mapper.RolMapper;
import com.buenrostroasociados.gestion_clientes.repository.RolRepository;
import com.buenrostroasociados.gestion_clientes.service.RolService;
import com.buenrostroasociados.gestion_clientes.service.export.ExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolServiceImpl implements RolService {

    private static final Logger logger = LoggerFactory.getLogger(RolServiceImpl.class);

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private RolMapper rolMapper;

    @Autowired
    private ExportService exportService;

    @Transactional
    @Override
    public RolDTO createRol(RolDTO rolDTO) {
        // Convertir nombre del rol a mayúsculas
        String rolNombreUpper = rolDTO.getNombre().toUpperCase();

        // Verificar si el rol ya existe en la base de datos
        if (rolRepository.existsByNombre(rolNombreUpper)) {
            throw new IllegalArgumentException("El rol ya existe.");
        }

        // Convertir DTO a entidad
        Rol rol = rolMapper.toEntity(rolDTO);
        rol.setNombre(rolNombreUpper); // Asegurar que el nombre está en mayúsculas

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
        List<Rol> roles = rolRepository.findAll();

        if (roles.isEmpty()){
            throw new EntityNotFoundException("No se encontro ningun rol en el Repositorio");
        }
        return roles.stream()
                .map(rolMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public RolDTO updateRol(Long id, RolDTO rolDTO) {
        // Validar que el rol existe en el enum
        String rolNombreUpper = rolDTO.getNombre().toUpperCase();

        // Buscar el rol existente
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));

        // Actualizar el rol
        rol.setNombre(rolNombreUpper);
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
    @Override
    public Resource exportActividadesToCSV() {
        List<RolDTO> roles = getAllRoles();
        List<String> headers = List.of("ID", "Nombrerol");
        List<List<String>> data = roles.stream()
                .map(rol -> List.of(
                        rol.getId().toString(),
                        rol.getNombre().toString()
                ))
                .collect(Collectors.toList());

        if (data.isEmpty()) {
            logger.warn("No hay datos para exportar al CSV.");
        }

        return exportService.exportToCSV(headers, data);
    }
    @Override
    public Resource exportActividadesToPDF() {
        List<RolDTO> roles = getAllRoles();
        List<String> headers = List.of("ID", "Nombre");
        List<List<String>> data = roles.stream()
                .map(rol-> List.of(
                        rol.getId().toString(),
                        rol.getNombre().toString()
                ))
                .collect(Collectors.toList());

        String title = "Reporte de roles";
        return exportService.exportToPDF(title, headers, data);
    }
}
