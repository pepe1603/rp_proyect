package com.buenrostroasociados.gestion_clientes.service.impl;

import com.buenrostroasociados.gestion_clientes.dto.ActividadContableDTO;
import com.buenrostroasociados.gestion_clientes.entity.ActividadContable;
import com.buenrostroasociados.gestion_clientes.entity.Archivo;
import com.buenrostroasociados.gestion_clientes.entity.Cliente;
import com.buenrostroasociados.gestion_clientes.events.actividad.contable.ActividadContableActualizadaEvent;
import com.buenrostroasociados.gestion_clientes.events.actividad.contable.ActividadContableCreadaEvent;
import com.buenrostroasociados.gestion_clientes.events.actividad.contable.ActividadContableEliminadaEvent;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.mapper.ActividadContableMapper;
import com.buenrostroasociados.gestion_clientes.notification.NotificationService;
import com.buenrostroasociados.gestion_clientes.repository.ActividadContableRepository;
import com.buenrostroasociados.gestion_clientes.repository.ArchivoRepository;
import com.buenrostroasociados.gestion_clientes.repository.ClienteRepository;
import com.buenrostroasociados.gestion_clientes.service.ActividadContableService;
import com.buenrostroasociados.gestion_clientes.service.export.ExportService;
import com.buenrostroasociados.gestion_clientes.service.files.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ActividadContableServiceImpl implements ActividadContableService {
    private static final Logger logger= LoggerFactory.getLogger(ActividadContableServiceImpl.class);

    @Autowired
    private ActividadContableMapper actividadContableMapper;
    @Autowired
    private ActividadContableRepository actividadContableRepo;
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
    public ActividadContableDTO saveActividadContable(ActividadContableDTO actividadContableDTO) {
        Cliente cliente = clienteRepo.findById(actividadContableDTO.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + actividadContableDTO.getClienteId()));

        actividadContableDTO.setFechaCreacion(LocalDateTime.now());
        ActividadContable actividadContable = actividadContableMapper.toEntity(actividadContableDTO);
        actividadContable.setCliente(cliente);

        // No s emanejan archivosa enla cracion inicial
        ActividadContable actividadGuardada = actividadContableRepo.save(actividadContable);

        //publicar evento de creacion
        eventPublisher.publishEvent(new ActividadContableCreadaEvent(this, actividadContable.getTitulo()));
        notificationService.notifyActivityContableCreation(cliente.getCorreo(), actividadContable.getTitulo());

        return actividadContableMapper.toDTO(actividadGuardada);
    }
    @Override
    public ActividadContableDTO getActividadContableById(Long id) {
        ActividadContable actividadContable = actividadContableRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actividad contable no encontrada con ID: " + id));

        return actividadContableMapper.toDTO(actividadContable);
    }

    @Override
    public List<ActividadContableDTO> getAllActividadesContables() {
        List<ActividadContable> actividadesContables = actividadContableRepo.findAll();
        if (actividadesContables.isEmpty()) {
            throw new EntityNotFoundException("No se encontro ninguna Actividad Contable en el Repositorio");
        }

        return actividadesContables.stream()
                .map(actividadContableMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ActividadContableDTO updateActividadContable(Long id, ActividadContableDTO actividadContableDTO) {
        // Buscar la entidad existente por ID
        ActividadContable actividadContable = actividadContableRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actividad contable no encontrada con ID: " + id));

        // Actualizar los campos de la entidad existente
        actividadContableMapper.updateEntity(actividadContableDTO, actividadContable);

        // Buscar y asignar el cliente
        Cliente cliente = clienteRepo.findById(actividadContableDTO.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + id));
        actividadContable.setCliente(cliente);

        // Guardar la entidad actualizada
        ActividadContable actividadActualizada = actividadContableRepo.save(actividadContable);

        //publicar evento de creacion
        eventPublisher.publishEvent(new ActividadContableActualizadaEvent(this, actividadContable.getTitulo() +"\n Revisa Los cambios en nuestra pĺataforma."));
        notificationService.notifyActivityContableUpdate(cliente.getCorreo(), actividadContable.getTitulo() +"\n Revisa Los cambios en nuestra pĺataforma.");

        return actividadContableMapper.toDTO(actividadActualizada);
    }

    @Override
    public void deleteActividadContable(Long id) {
        ActividadContable actividadContable = actividadContableRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actividad Contable no encontrada con ID: " + id));

        // Elimina los archivos asociados
        List<Archivo> archivos = archivoRepo.findArchivosByActividadContableId(id);
        for (Archivo archivo : archivos) {
            fileService.delete(archivo.getNombreArchivo());
            archivoRepo.delete(archivo);
        }

        // Elimina la actividad contable
        actividadContableRepo.delete(actividadContable);
        //notificaon de Eliminacion
        eventPublisher.publishEvent(new ActividadContableEliminadaEvent(this, actividadContable.getTitulo()));
    }

    @Override
    public Resource exportActividadesToCSV() {
        List<ActividadContableDTO> actividadesContables = getAllActividadesContables();
        List<String> headers = List.of("ID", "Titulo", "Descripcion", "FechaCreacion", "ClienteId");
        List<List<String>> data = actividadesContables.stream()
                .map(actividad -> List.of(
                        actividad.getId().toString(),
                        actividad.getTitulo().toString(),
                        actividad.getDescripcion().toString(),
                        actividad.getFechaCreacion().toString(),
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
        List<ActividadContableDTO> actividadesContables = getAllActividadesContables();
        List<String> headers = List.of("ID", "Titulo", "Descripcion", "FechaCreacion", "CleinteId");
        List<List<String>> data = actividadesContables.stream()
                .map(actividad -> List.of(
                        actividad.getId().toString(),
                        actividad.getTitulo(),
                        actividad.getDescripcion().toString(),
                        actividad.getFechaCreacion().toString(),
                        actividad.getClienteId().toString()
                ))
                .collect(Collectors.toList());

        String title = "Reporte de Actividades Contables";
        return exportService.exportToPDF(title, headers, data);
    }


}
