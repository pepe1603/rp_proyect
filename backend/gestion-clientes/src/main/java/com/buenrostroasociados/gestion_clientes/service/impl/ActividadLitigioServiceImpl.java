package com.buenrostroasociados.gestion_clientes.service.impl;

import com.buenrostroasociados.gestion_clientes.dto.ActividadLitigioDTO;
import com.buenrostroasociados.gestion_clientes.entity.ActividadLitigio;
import com.buenrostroasociados.gestion_clientes.entity.Archivo;
import com.buenrostroasociados.gestion_clientes.entity.Cliente;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.mapper.ActividadLitigioMapper;
import com.buenrostroasociados.gestion_clientes.repository.ActividadLitigioRepository;
import com.buenrostroasociados.gestion_clientes.repository.ArchivoRepository;
import com.buenrostroasociados.gestion_clientes.repository.ClienteRepository;
import com.buenrostroasociados.gestion_clientes.service.ActividadLitigioService;
import com.buenrostroasociados.gestion_clientes.service.files.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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

        // Manejo de archivos (si aplica)
        if (actividadLitigioDTO.getDocumentos() != null) {
            List<Archivo> archivos = actividadLitigioDTO.getDocumentos().stream()
                    .map(archivoDTO -> {
                        Archivo archivo = new Archivo();
                        archivo.setNombreArchivo(archivoDTO.getNombreArchivo());
                        archivo.setRutaArchivo(fileService.getRutaArchivo(archivoDTO.getNombreArchivo()));
                        archivo.setActividadLitigio(actividadLitigio); // Asignar actividad litigio
                        return archivo;
                    })
                    .collect(Collectors.toList());
            actividadLitigio.setDocumentos(archivos);
        }

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
        ActividadLitigio actividadLitigio = actividadLitigioRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actividad litigio no encontrada con ID: " + id));

        actividadLitigioMapper.updateEntity(actividadLitigioDTO, actividadLitigio);

        Cliente cliente = clienteRepo.findById(actividadLitigioDTO.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + actividadLitigioDTO.getClienteId()));
        actividadLitigio.setCliente(cliente);

        // Manejo de archivos
        if (actividadLitigioDTO.getDocumentos() != null) {
            List<Archivo> archivos = actividadLitigioDTO.getDocumentos().stream()
                    .map(archivoDTO -> {
                        Archivo archivo = new Archivo();
                        archivo.setNombreArchivo(archivoDTO.getNombreArchivo());
                        archivo.setRutaArchivo(fileService.getRutaArchivo(archivoDTO.getNombreArchivo()));
                        archivo.setActividadLitigio(actividadLitigio);
                        return archivo;
                    })
                    .collect(Collectors.toList());
            actividadLitigio.setDocumentos(archivos);
        }

        ActividadLitigio actividadActualizada = actividadLitigioRepo.save(actividadLitigio);
        return actividadLitigioMapper.toDTO(actividadActualizada);
    }

    @Override
    public ActividadLitigioDTO updateActividadLitigio(Long id, Map<String, Object> updates) {
        ActividadLitigio actividadLitigio = actividadLitigioRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actividad litigio no encontrada con ID: " + id));

        // Aquí podrías implementar lógica para aplicar actualizaciones parciales si es necesario

        ActividadLitigio actividadActualizada = actividadLitigioRepo.save(actividadLitigio);
        return actividadLitigioMapper.toDTO(actividadActualizada);
    }

    @Override
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
}
