package com.buenrostroasociados.gestion_clientes.service.impl;

import com.buenrostroasociados.gestion_clientes.dto.AdministradorDTO;
import com.buenrostroasociados.gestion_clientes.entity.Administrador;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.mapper.AdministradorMapper;
import com.buenrostroasociados.gestion_clientes.repository.AdministradorRepository;
import com.buenrostroasociados.gestion_clientes.repository.ClienteRepository;
import com.buenrostroasociados.gestion_clientes.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdministradorServiceImpl implements AdministradorService {
    @Autowired
    private AdministradorRepository administradorRepo;
    @Autowired
    private AdministradorMapper administradorMapper;
    @Autowired
    private ClienteRepository clienteRepo;

    @Override
    public AdministradorDTO savedAdministrador(AdministradorDTO administradorDTO) {
        Administrador newAdministrador = administradorMapper.toEntity(administradorDTO);
        newAdministrador.setUsuario(null);

        Administrador savedAdministrador = administradorRepo.save(newAdministrador);

        return administradorMapper.toDTO(savedAdministrador);
    }

    @Override
    public AdministradorDTO getAdministradorById(Long id) {
        Administrador adminFounded=administradorRepo.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cliente no encontrado con ID: "+id)
                );

        return administradorMapper.toDTO(adminFounded);

    }

    @Override
    public List<AdministradorDTO> getAllAdministradores() {
        List<Administrador> administradores = administradorRepo.findAll();

        if (administradores.isEmpty()){
            throw new EntityNotFoundException("No se encontro ningun Administrador en el repositorio");
        }

        return administradores.stream()
                .map(administradorMapper::toDTO)
                .collect(Collectors.toList()
                );
    }

    @Override
    public AdministradorDTO getAdministradorByEmail(String email) {
        Administrador admin = administradorRepo.findByCorreo(email)
                .orElseThrow( () -> new EntityNotFoundException("Administrrador no encontrado con email: "+email));

        return administradorMapper.toDTO(admin);
    }

    @Override
    public AdministradorDTO updateClaveAdministrador(Long id, String clave) {
        Administrador adminFounded=administradorRepo.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cliente no encontrado con ID: "+id)
                );
        adminFounded.setClave(clave);

        Administrador savedAdmin = administradorRepo.save(adminFounded);
        return administradorMapper.toDTO(savedAdmin);
    }

    @Override
    public AdministradorDTO updateAdministrador(Long id, AdministradorDTO administradorDTO) {
        Administrador adminFounded=administradorRepo.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cliente no encontrado con ID: "+id)
                );
        adminFounded.setClave(administradorDTO.getClave());
        adminFounded.setUsuario(null);
        adminFounded.setCorreo(administradorDTO.getCorreo());
        adminFounded.setTelefono(administradorDTO.getTelefono());
        adminFounded.setNombreFull(administradorDTO.getNombreFull());

        Administrador savedAdmin = administradorRepo.save(adminFounded);

        return administradorMapper.toDTO(savedAdmin);
    }

    @Override
    public void deleteAdministrador(Long id) {
        Administrador adminFounded=administradorRepo.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cliente no encontrado con ID: "+id)
                );
        administradorRepo.deleteById(id);
    }
}
