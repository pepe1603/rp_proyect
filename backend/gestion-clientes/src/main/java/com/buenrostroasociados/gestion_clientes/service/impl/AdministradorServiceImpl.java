package com.buenrostroasociados.gestion_clientes.service.impl;

import com.buenrostroasociados.gestion_clientes.dto.AdministradorDTO;
import com.buenrostroasociados.gestion_clientes.entity.Administrador;
import com.buenrostroasociados.gestion_clientes.entity.Usuario;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.mapper.AdministradorMapper;
import com.buenrostroasociados.gestion_clientes.repository.AdministradorRepository;
import com.buenrostroasociados.gestion_clientes.repository.ClienteRepository;
import com.buenrostroasociados.gestion_clientes.repository.UsuarioRepository;
import com.buenrostroasociados.gestion_clientes.service.AdministradorService;
import com.buenrostroasociados.gestion_clientes.service.export.ExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    private static final Logger logger = LoggerFactory.getLogger(AdministradorServiceImpl.class);
    @Autowired
    private AdministradorRepository administradorRepo;
    @Autowired
    private AdministradorMapper administradorMapper;
    @Autowired
    private ClienteRepository clienteRepo;
    @Autowired
    private ExportService exportService;
    @Autowired
    private UsuarioRepository usuarioRepo;

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
        adminFounded.setCorreo(administradorDTO.getCorreo());
        adminFounded.setTelefono(administradorDTO.getTelefono());
        adminFounded.setNombreFull(administradorDTO.getNombreFull());

        Administrador updatedAdmin = administradorRepo.save(adminFounded);
        //una vez que el cleinte ya ha sido actualizadoc on exito actaulizamos el email del usuario

        logger.warn("Verificando actualizacion del usuario si esta disponible");
        if (!(administradorDTO.getUsuarioId() == null)){
            Usuario usuarioFounded = usuarioRepo.findById(administradorDTO.getUsuarioId()).orElseThrow(
                    () -> new EntityNotFoundException("Usuario no encontrado Para Administrador : "+administradorDTO.getUsuarioId())
            ) ;

            usuarioFounded.setEmail(updatedAdmin.getCorreo());
            logger.warn("Correo del usuario actualizado automaticamente");
        }

        return administradorMapper.toDTO(updatedAdmin);
    }

    @Override
    public void deleteAdministrador(Long id) {
        Administrador adminFounded=administradorRepo.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cliente no encontrado con ID: "+id)
                );
        administradorRepo.deleteById(id);
    }

    @Override
    public Resource exportActividadesToCSV() {
        List<AdministradorDTO> administradores = getAllAdministradores();
        List<String> headers = List.of("ID", "Clave", "NombreCompleto", "Correo", "Telefono", "UsuarioId");
        List<List<String>> data = administradores.stream()
                .map(admin -> List.of(
                        admin.getId().toString(),
                        admin.getClave().toString(),
                        admin.getNombreFull().toString(),
                        admin.getCorreo().toString(),
                        admin.getCorreo().toString(),
                        admin.getTelefono().toString(),
                        admin.getUsuarioId().toString()
                ))
                .collect(Collectors.toList());

        if (data.isEmpty()) {
            logger.warn("No hay datos para exportar al CSV.");
        }

        return exportService.exportToCSV(headers, data);
    }

    @Override
    public Resource exportActividadesToPDF() {
        List<AdministradorDTO> administradores = getAllAdministradores();
        List<String> headers = List.of("ID", "RFC", "Nombre", "Correo", "Telefono", "UsuarioId");
        List<List<String>> data = administradores.stream()
                .map(admin -> List.of(
                        admin.getId().toString(),
                        admin.getClave().toString(),
                        admin.getNombreFull().toString(),
                        admin.getCorreo().toString(),
                        admin.getCorreo().toString(),
                        admin.getTelefono().toString(),
                        admin.getUsuarioId().toString()
                ))
                .collect(Collectors.toList());

        String title = "Reporte de Administradores";
        return exportService.exportToPDF(title, headers, data);
    }
}
