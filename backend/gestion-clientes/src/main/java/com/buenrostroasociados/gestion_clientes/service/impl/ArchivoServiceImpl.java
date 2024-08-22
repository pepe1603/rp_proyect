package com.buenrostroasociados.gestion_clientes.service.impl;

import com.buenrostroasociados.gestion_clientes.dto.ArchivoDTO;
import com.buenrostroasociados.gestion_clientes.entity.ActividadContable;
import com.buenrostroasociados.gestion_clientes.entity.ActividadLitigio;
import com.buenrostroasociados.gestion_clientes.entity.Archivo;
import com.buenrostroasociados.gestion_clientes.exception.ResourceNotFoundException;
import com.buenrostroasociados.gestion_clientes.mapper.ArchivoMapper;
import com.buenrostroasociados.gestion_clientes.repository.ActividadContableRepository;
import com.buenrostroasociados.gestion_clientes.repository.ActividadLitigioRepository;
import com.buenrostroasociados.gestion_clientes.repository.ArchivoRepository;
import com.buenrostroasociados.gestion_clientes.service.ArchivoService;
import com.buenrostroasociados.gestion_clientes.service.files.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileSystemAlreadyExistsException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class ArchivoServiceImpl implements ArchivoService {

    @Autowired
    private ArchivoRepository archivoRepository;

    @Autowired
    private ActividadContableRepository actividadContableRepository;

    @Autowired
    private ActividadLitigioRepository actividadLitigioRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ArchivoMapper archivoMapper;

    @Override
    public ArchivoDTO saveArchivo(ArchivoDTO archivoDTO, MultipartFile file, boolean replaceExisting) {
        // Obtiene las entidades de ActividadContable y ActividadLitigio si se especifican
        ActividadContable actividadContable = null;
        ActividadLitigio actividadLitigio = null;

        if (archivoDTO.getActividadContableId() != null) {
            actividadContable = actividadContableRepository.findById(archivoDTO.getActividadContableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Actividad Contable no encontrada con id: " + archivoDTO.getActividadContableId()));
        }

        if (archivoDTO.getActividadLitigioId() != null) {
            actividadLitigio = actividadLitigioRepository.findById(archivoDTO.getActividadLitigioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Actividad Litigio no encontrada con id: " + archivoDTO.getActividadLitigioId()));
        }

        // Verifica si el archivo ya existe
        Optional<Archivo> existingArchivo = archivoRepository.findByNombreArchivo(file.getOriginalFilename());

        String filename = fileService.getUniqueFilename(file.getOriginalFilename());

        // Si el archivo ya existe y se debe reemplazar, elimina el archivo antiguo
        if (existingArchivo.isPresent()) {
            if (replaceExisting) {
                // Elimina el archivo del sistema de archivos local
                fileService.delete(existingArchivo.get().getNombreArchivo());
                archivoRepository.delete(existingArchivo.get());
            } else {
                throw new FileSystemAlreadyExistsException("El archivo con el nombre " + file.getOriginalFilename() + " ya existe.");
            }
        }

        // Guarda el archivo en el sistema de archivos local
        fileService.save(file, filename);

        // Mapea el DTO a la entidad y guarda en la base de datos
        Archivo archivo = archivoMapper.toEntity(archivoDTO);
        archivo.setNombreArchivo(filename);
        archivo.setRutaArchivo(fileService.getRutaArchivo(filename));
        archivo.setActividadContable(actividadContable);
        archivo.setActividadLitigio(actividadLitigio);

        Archivo savedArchivo = archivoRepository.save(archivo);

        return archivoMapper.toDTO(savedArchivo);
    }

    @Override
    public ArchivoDTO getArchivo(Long id) {
        Archivo archivo = archivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Archivo no encontrado con id: " + id));
        return archivoMapper.toDTO(archivo);
    }

    @Override
    public List<ArchivoDTO> getAllArchivos() {
        return archivoRepository.findAll().stream()
                .map(archivoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteArchivo(Long id) {
        Archivo archivo = archivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("{El archivo no pudo ser eliminado por que no fue encontrado con id: " + id));

        // Elimina el archivo del sistema de archivos local
        fileService.delete(archivo.getNombreArchivo());

        // Elimina el registro en la base de datos
        archivoRepository.delete(archivo);
    }
}
