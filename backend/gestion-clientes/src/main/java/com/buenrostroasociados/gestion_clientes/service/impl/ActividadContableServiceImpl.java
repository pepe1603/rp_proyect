package com.buenrostroasociados.gestion_clientes.service.impl;

import com.buenrostroasociados.gestion_clientes.dto.ActividadContableDTO;
import com.buenrostroasociados.gestion_clientes.entity.ActividadContable;
import com.buenrostroasociados.gestion_clientes.entity.Archivo;
import com.buenrostroasociados.gestion_clientes.entity.Cliente;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.mapper.ActividadContableMapper;
import com.buenrostroasociados.gestion_clientes.repository.ActividadContableRepository;
import com.buenrostroasociados.gestion_clientes.repository.ArchivoRepository;
import com.buenrostroasociados.gestion_clientes.repository.ClienteRepository;
import com.buenrostroasociados.gestion_clientes.service.ActividadContableService;
import com.buenrostroasociados.gestion_clientes.service.files.FileService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class ActividadContableServiceImpl implements ActividadContableService {
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

    @Override
    public ActividadContableDTO saveActividadContable(ActividadContableDTO actividadContableDTO) {
        Cliente cliente = clienteRepo.findById(actividadContableDTO.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + actividadContableDTO.getClienteId()));

        ActividadContable actividadContable = actividadContableMapper.toEntity(actividadContableDTO);
        actividadContable.setCliente(cliente);

        // Manejo de archivos (si aplica)
        if (actividadContableDTO.getArchivos() != null) {
            List<Archivo> archivos = actividadContableDTO.getArchivos().stream()
                    .map(archivoDTO -> {
                        Archivo archivo = new Archivo();
                        archivo.setNombreArchivo(archivoDTO.getNombreArchivo());
                        archivo.setRutaArchivo(fileService.getRutaArchivo(archivoDTO.getNombreArchivo()));
                        archivo.setActividadContable(actividadContable); // Asignar actividad contable
                        return archivo;
                    })
                    .collect(Collectors.toList());
            actividadContable.setArchivos(archivos);
        }

        ActividadContable actividadGuardada = actividadContableRepo.save(actividadContable);
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
            throw new EntityNotFoundException("No hay Actividades Contables en el Repositorio");
        }

        return actividadesContables.stream()
                .map(actividadContableMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ActividadContableDTO updateActividadContable(Long id, ActividadContableDTO actividadContableDTO) {
        ActividadContable actividadContable = actividadContableRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actividad contable no encontrada con ID: " + id));

        actividadContableMapper.updateEntity(actividadContableDTO, actividadContable);

        Cliente cliente = clienteRepo.findById(actividadContableDTO.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + actividadContableDTO.getClienteId()));
        actividadContable.setCliente(cliente);

        // Manejo de archivos
        if (actividadContableDTO.getArchivos() != null) {
            List<Archivo> archivos = actividadContableDTO.getArchivos().stream()
                    .map(archivoDTO -> {
                        Archivo archivo = new Archivo();
                        archivo.setNombreArchivo(archivoDTO.getNombreArchivo());
                        archivo.setRutaArchivo(fileService.getRutaArchivo(archivoDTO.getNombreArchivo()));
                        archivo.setActividadContable(actividadContable);
                        return archivo;
                    })
                    .collect(Collectors.toList());
            actividadContable.setArchivos(archivos);
        }

        ActividadContable actividadActualizada = actividadContableRepo.save(actividadContable);
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
    }
}
