package com.buenrostroasociados.gestion_clientes.service.impl;

import com.buenrostroasociados.gestion_clientes.dto.ActividadLitigioDTO;
import com.buenrostroasociados.gestion_clientes.entity.ActividadLitigio;
import com.buenrostroasociados.gestion_clientes.entity.Archivo;
import com.buenrostroasociados.gestion_clientes.entity.Cliente;
import com.buenrostroasociados.gestion_clientes.entity.Usuario;
import com.buenrostroasociados.gestion_clientes.enums.EstadoCaso;
import com.buenrostroasociados.gestion_clientes.events.actividad.litigio.ActividadLitigioActualizadaEvent;
import com.buenrostroasociados.gestion_clientes.events.actividad.litigio.ActividadLitigioCreadaEvent;
import com.buenrostroasociados.gestion_clientes.events.actividad.litigio.ActividadLitigioEliminadaEvent;
import com.buenrostroasociados.gestion_clientes.exception.BusinessException;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.exception.UnauthorizedException;
import com.buenrostroasociados.gestion_clientes.mapper.ActividadLitigioMapper;
import com.buenrostroasociados.gestion_clientes.notification.NotificationService;
import com.buenrostroasociados.gestion_clientes.repository.ActividadLitigioRepository;
import com.buenrostroasociados.gestion_clientes.repository.ArchivoRepository;
import com.buenrostroasociados.gestion_clientes.repository.ClienteRepository;
import com.buenrostroasociados.gestion_clientes.service.ActividadLitigioService;
import com.buenrostroasociados.gestion_clientes.service.export.ExportService;
import com.buenrostroasociados.gestion_clientes.service.files.FileService;
import com.buenrostroasociados.gestion_clientes.utils.CurrentUserAuthenticated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActividadLitigioServiceImpl implements ActividadLitigioService {

    private static final Logger logger = LoggerFactory.getLogger(ActividadContableServiceImpl.class);

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
    @Autowired
    private ExportService exportService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public ActividadLitigioDTO saveActividadLitigio(ActividadLitigioDTO actividadLitigioDTO) {
        Cliente cliente = clienteRepo.findById(actividadLitigioDTO.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + actividadLitigioDTO.getClienteId()));

        actividadLitigioDTO.setFechaCreacion(LocalDateTime.now());
        ActividadLitigio actividadLitigio = actividadLitigioMapper.toEntity(actividadLitigioDTO);
        actividadLitigio.setCliente(cliente);
        actividadLitigio.setEstadoCaso(EstadoCaso.PENDIENTE);

        // No se manejan archivos en la creación inicial
        ActividadLitigio savedActividad = actividadLitigioRepo.save(actividadLitigio);

        //publicamos evento de creacion
        eventPublisher.publishEvent(new ActividadLitigioCreadaEvent(this, actividadLitigio.getTitulo()));
        notificationService.notifyActivityLitigioCreation(cliente.getCorreo(), savedActividad.getTitulo());

        return actividadLitigioMapper.toDTO(savedActividad);
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
            throw new EntityNotFoundException("No se encontro ninguna actividad litigio en el Repositorio");
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
        //publicar Evento de Actrualizacion
        eventPublisher.publishEvent(new ActividadLitigioActualizadaEvent(this, actividadActualizada.getTitulo()));
        notificationService.notifyActivityLitigioUpdate(cliente.getCorreo(), actividadLitigio.getTitulo());

        // Convertir la entidad actualizada a DTO y devolver
        return actividadLitigioMapper.toDTO(actividadActualizada);
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

        /*
         * Obtén el usuario actual desde la autenticación
         */
        Usuario currentUser = CurrentUserAuthenticated.getCurrentUser(); // Método para obtener el usuario actual
        checkUserPermission(currentUser, nuevoEstado);
        checkBusinessRules(actividadLitigio, nuevoEstado);//omitir este metodod

        // Actualizar el estado
        actividadLitigio.setEstadoCaso(nuevoEstado);
        actividadLitigioRepo.save(actividadLitigio);

        //publicar evento a clientes
        notificationService.notifyActivityLitigioUpdatedStatus(actividadLitigio.getCliente().getCorreo(),
                actividadLitigio.getTitulo() + " con No.Expediente [ "+actividadLitigio.getNumExpediente()+" ] ha cambiado de estado a "+actividadLitigio.getEstadoCaso().toString()+"." );
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
        //publicar evento de elimnacion
        eventPublisher.publishEvent(new ActividadLitigioEliminadaEvent(this, actividadLitigio.getTitulo()));
        notificationService.notifyActivityLitigioDeletion(actividadLitigio.getCliente().getCorreo(), actividadLitigio.getTitulo());
    }



    @Override
    public Resource exportActividadesToCSV() {
        List<ActividadLitigioDTO> actividades = getAllActividadesLitigio();
        List<String> headers = List.of("ID", "Title",  "Descripción", "NumExpediante", "Actor", "Fecha Creación", "Estado Caso", "Cliente ID");
        List<List<String>> data = actividades.stream()
                .map(actividad -> List.of(
                        actividad.getId().toString(),
                        actividad.getTitulo(),
                        actividad.getDescripcion(),
                        actividad.getNumExpediente().toString(),
                        actividad.getActor().toString(),
                        actividad.getFechaCreacion().toString(),
                        actividad.getEstadoCaso(),
                        actividad.getClienteId().toString()
                ))
                .collect(Collectors.toList());

        if (data.isEmpty()) {
            logger.warn("No hay datos para exportar al CSV.");
        }

        return exportService.exportToCSV(headers, data);
    }

    @Override
    public Resource exportActividadesToPDF() {
        List<ActividadLitigioDTO> actividades = getAllActividadesLitigio();
        List<String> headers = List.of("ID", "Title",  "Descripción", "NumExpediante", "Actor", "Fecha Creación", "Estado Caso", "Cliente ID");
        List<List<String>> data = actividades.stream()
                .map(actividad -> List.of(
                        actividad.getId().toString(),
                        actividad.getTitulo(),
                        actividad.getDescripcion(),
                        actividad.getNumExpediente().toString(),
                        actividad.getActor().toString(),
                        actividad.getFechaCreacion().toString(),
                        actividad.getEstadoCaso(),
                        actividad.getClienteId().toString()
                ))
                .collect(Collectors.toList());

        String title = "Reporte de Actividades de Litigio";
        return exportService.exportToPDF(title, headers, data);
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
                throw new IllegalArgumentException("Valor no válido para la transicion del Estado del Caso: " + estadoActual);
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
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));

        if (nuevoEstado == EstadoCaso.RESUELTO && !hasRoleAdmin) {
            throw new UnauthorizedException("No tiene permisos para cambiar el estado a"+ EstadoCaso.RESUELTO);
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

}
