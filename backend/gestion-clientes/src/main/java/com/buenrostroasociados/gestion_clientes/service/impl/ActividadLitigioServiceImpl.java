package com.buenrostroasociados.gestion_clientes.service.impl;

import com.buenrostroasociados.gestion_clientes.dto.ActividadLitigioDTO;
import com.buenrostroasociados.gestion_clientes.entity.ActividadLitigio;
import com.buenrostroasociados.gestion_clientes.entity.Archivo;
import com.buenrostroasociados.gestion_clientes.entity.Cliente;
import com.buenrostroasociados.gestion_clientes.entity.Usuario;
import com.buenrostroasociados.gestion_clientes.enums.EstadoCaso;
import com.buenrostroasociados.gestion_clientes.exception.BusinessException;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.exception.UnauthorizedException;
import com.buenrostroasociados.gestion_clientes.mapper.ActividadLitigioMapper;
import com.buenrostroasociados.gestion_clientes.repository.ActividadLitigioRepository;
import com.buenrostroasociados.gestion_clientes.repository.ArchivoRepository;
import com.buenrostroasociados.gestion_clientes.repository.ClienteRepository;
import com.buenrostroasociados.gestion_clientes.service.ActividadLitigioService;
import com.buenrostroasociados.gestion_clientes.service.files.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActividadLitigioServiceImpl implements ActividadLitigioService {
    @Autowired
    private ActividadLitigioMapper actividadLitigioMapper;
    @Autowired
    private ActividadLitigioRepository actividadLitigioRepo;
    @Autowired
    private ClienteRepository clienteRepo;
    @Autowired
    private ArchivoRepository archivoRepo;
    @Autowired
    private FileService fileService;

    @Override
    public ActividadLitigioDTO saveActividadLitigio(ActividadLitigioDTO actividadLitigioDTO) {
        Cliente cliente = clienteRepo.findById(actividadLitigioDTO.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + actividadLitigioDTO.getClienteId()));

        ActividadLitigio actividadLitigio = actividadLitigioMapper.toEntity(actividadLitigioDTO);
        actividadLitigio.setCliente(cliente);
        actividadLitigio.setEstadoCaso(EstadoCaso.PENDIENTE);

        // No se manejan archivos en la creación inicial
        ActividadLitigio actividadGuardada = actividadLitigioRepo.save(actividadLitigio);
        return actividadLitigioMapper.toDTO(actividadGuardada);
    }

    @Override
    public ActividadLitigioDTO getActividadLitigioById(Long id) {
        ActividadLitigio actividadLitigio = actividadLitigioRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actividad litigio no encontrada con ID: " + id));

        return actividadLitigioMapper.toDTO(actividadLitigio);
    }

    @Override
    public List<ActividadLitigioDTO> getAllActividadesLitigio() {
        List<ActividadLitigio> actividadesLitigio = actividadLitigioRepo.findAll();
        if (actividadesLitigio.isEmpty()) {
            throw new EntityNotFoundException("No hay Actividades Litigio en el Repositorio");
        }

        return actividadesLitigio.stream()
                .map(actividadLitigioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ActividadLitigioDTO updateActividadLitigio(Long id, ActividadLitigioDTO actividadLitigioDTO) {
        // Buscar la entidad existente por ID
        ActividadLitigio actividadLitigio = actividadLitigioRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actividad litigio no encontrada con ID: " + id));

        // Actualizar los campos de la entidad existente
        actividadLitigioMapper.updateEntity(actividadLitigioDTO, actividadLitigio);

        // Buscar y asignar el cliente
        Cliente cliente = clienteRepo.findById(actividadLitigioDTO.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + id));
        actividadLitigio.setCliente(cliente);

        // Guardar la entidad actualizada
        ActividadLitigio actividadActualizada = actividadLitigioRepo.save(actividadLitigio);

        // Convertir la entidad actualizada a DTO y devolver
        return actividadLitigioMapper.toDTO(actividadActualizada);
    }

    @Override
    public void updateActividadLitigioFiles(Long id, List<Archivo> archivosDTO) {
        ActividadLitigio actividadLitigio = actividadLitigioRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actividad litigio no encontrada con ID: " + id));

        // Eliminar documentos antiguos si es necesario
        List<Archivo> archivosExistentes = archivoRepo.findArchivosByActividadLitigioId(id);
        for (Archivo archivo : archivosExistentes) {
            fileService.delete(archivo.getNombreArchivo());
            archivoRepo.delete(archivo);
        }

        // Añadir nuevos documentos
        if (archivosDTO != null) {
            List<Archivo> archivos = archivosDTO.stream()
                    .map(archivoDTO -> {
                        Archivo archivo = new Archivo();
                        archivo.setNombreArchivo(archivoDTO.getNombreArchivo());
                        archivo.setRutaArchivo(fileService.getRutaArchivo(archivoDTO.getNombreArchivo()));
                        archivo.setActividadLitigio(actividadLitigio);
                        return archivo;
                    })
                    .collect(Collectors.toList());

            archivoRepo.saveAll(archivos);
        }
    }

    @Override
    @Transactional
    public void updateEstadoActividadLitigio(Long id, String estadoCaso) {
        ActividadLitigio actividadLitigio = actividadLitigioRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actividad Litigio no encontrada con ID: " + id));

        EstadoCaso nuevoEstado = EstadoCaso.valueOf(estadoCaso);

        // Validaciones
        if (!isTransitionValid(actividadLitigio.getEstadoCaso(), nuevoEstado)) {
            throw new BusinessException("Transición de estado no válida");
        }

        Usuario currentUser = getCurrentUser(); // Método para obtener el usuario actual
        checkUserPermission(currentUser, nuevoEstado);
        checkBusinessRules(actividadLitigio, nuevoEstado);//omitir este metodod

        // Actualizar el estado
        actividadLitigio.setEstadoCaso(nuevoEstado);
        actividadLitigioRepo.save(actividadLitigio);
    }


    @Override
    @Transactional
    public void deleteActividadLitigio(Long id) {
        ActividadLitigio actividadLitigio = actividadLitigioRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actividad Litigio no encontrada con ID: " + id));

        // Elimina los archivos asociados
        List<Archivo> documentos = archivoRepo.findArchivosByActividadLitigioId(id);
        for (Archivo archivo : documentos) {
            fileService.delete(archivo.getNombreArchivo());
            archivoRepo.delete(archivo);
        }
        // Elimina la actividad litigio
        actividadLitigioRepo.delete(actividadLitigio);
    }

    /*
     * Métodos auxiliares
     */
    private boolean isTransitionValid(EstadoCaso estadoActual, EstadoCaso nuevoEstado) {
        if (estadoActual == null || nuevoEstado == null) {
            throw new IllegalArgumentException("Los estados no pueden ser nulos");
        }

        switch (estadoActual) {
            case PENDIENTE:
                // De "PENDIENTE" solo se puede cambiar a "PRESENTADO"
                return nuevoEstado == EstadoCaso.PRESENTADO;

            case PRESENTADO:
                // De "PRESENTADO" solo se puede cambiar a "EN_PROCESO"
                return nuevoEstado == EstadoCaso.EN_PROCESO;

            case EN_PROCESO:
                // De "EN_PROCESO" se puede cambiar a "RESUELTO" o "CERRADO"
                return nuevoEstado == EstadoCaso.RESUELTO || nuevoEstado == EstadoCaso.CERRADO;

            case RESUELTO:
                // De "RESUELTO" no se puede cambiar a ningún otro estado
                return false;

            case CERRADO:
                // De "CERRADO" no se puede cambiar a ningún otro estado
                return false;

            default:
                throw new IllegalArgumentException("Valor no válido para actualización de EstadoCaso: " + estadoActual);
        }
    }


    /*
     * Permisos del Usuario: Verifica si el usuario que solicita el cambio tiene los permisos necesarios para realizar esa acción.
     * Esto puede implicar comprobar roles o permisos de usuario.
     */
    private void checkUserPermission(Usuario user, EstadoCaso nuevoEstado) {
        // Lógica para verificar permisos del usuario
        // Ejemplo: Solo ciertos roles pueden cambiar el estado a "FINALIZADO"
        boolean hasRoleAdmin = user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (nuevoEstado == EstadoCaso.RESUELTO && !hasRoleAdmin) {
            throw new UnauthorizedException("No tiene permisos para cambiar el estado a FINALIZADO");
        }
    }

    /*
     * Condiciones de Negocio: Algunas transiciones de estado podrían requerir que se cumplan ciertas condiciones de negocio antes de permitir el cambio.
     */
    private void checkBusinessRules(ActividadLitigio actividadLitigio, EstadoCaso nuevoEstado) {
        // Lógica para verificar las reglas de negocio
        // Ejemplo: No se puede cambiar a "CERRADO" si hay archivos asociados
       /* if (nuevoEstado == EstadoCaso.CERRADO && !actividadLitigio.getArchivos().isEmpty()) {
            throw new BusinessException("No se puede cerrar la actividad con archivos asociados");
        }
        */

    }

    /*
     * Obtén el usuario actual desde la autenticación
     */
    private Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Usuario no autenticado");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof Usuario) {
            return (Usuario) principal;
        }else {
            throw new UnauthorizedException("Usuario no autenticado");
        }
    }

}
