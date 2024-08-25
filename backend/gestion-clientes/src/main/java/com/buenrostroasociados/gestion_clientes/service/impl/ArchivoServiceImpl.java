package com.buenrostroasociados.gestion_clientes.service.impl;

import com.buenrostroasociados.gestion_clientes.dto.ArchivoDTO;
import com.buenrostroasociados.gestion_clientes.entity.ActividadContable;
import com.buenrostroasociados.gestion_clientes.entity.ActividadLitigio;
import com.buenrostroasociados.gestion_clientes.entity.Archivo;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.exception.ResourceNotFoundException;
import com.buenrostroasociados.gestion_clientes.mapper.ArchivoMapper;
import com.buenrostroasociados.gestion_clientes.repository.ActividadContableRepository;
import com.buenrostroasociados.gestion_clientes.repository.ActividadLitigioRepository;
import com.buenrostroasociados.gestion_clientes.repository.ArchivoRepository;
import com.buenrostroasociados.gestion_clientes.service.ArchivoService;
import com.buenrostroasociados.gestion_clientes.service.export.ExportService;
import com.buenrostroasociados.gestion_clientes.service.files.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileSystemAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class ArchivoServiceImpl implements ArchivoService {

    private static final Logger logger = LoggerFactory.getLogger(ArchivoServiceImpl.class);

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

    @Autowired
    private ExportService exportService;

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


        archivoDTO.setFechaCreacion(LocalDateTime.now());// fecha actual por default
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
    public List<ArchivoDTO> getArchivosByActividadContableId(Long actividadContableId) {
        List<Archivo> archivos = archivoRepository.findArchivosByActividadContableId(actividadContableId);
        if (archivos.isEmpty()){
            throw new EntityNotFoundException("No se encontraron Archivos de Actvidad Contable");
        }
        return archivos.stream()
                .map(archivoMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<ArchivoDTO> getArchivosByActividadLitigioId(Long actividadLitigioId) {
        List<Archivo> archivos = archivoRepository.findArchivosByActividadLitigioId(actividadLitigioId);
        if (archivos.isEmpty()){
            throw new EntityNotFoundException("No se encontraron Archivos de Actvidad Litigio");
        }
        return archivos.stream()
                .map(archivoMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public ArchivoDTO updateArchivo(Long id, ArchivoDTO archivoDTO, MultipartFile file, boolean replaceExisting) {
        // Primero, encuentra el archivo existente
        Archivo existingArchivo = archivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Archivo no encontrado con id: " + id));

        // Verifica si el archivo ya existe en el sistema de archivos
        Optional<Archivo> oldArchivo = archivoRepository.findByNombreArchivo(file.getOriginalFilename());

        if (oldArchivo.isPresent() && replaceExisting) {
            // Elimina el archivo del sistema de archivos local
            fileService.delete(oldArchivo.get().getNombreArchivo());

            // Elimina el registro viejo en la base de datos
            archivoRepository.delete(oldArchivo.get());
        } else if (oldArchivo.isPresent()) {
            throw new FileSystemAlreadyExistsException("El archivo con el nombre " + file.getOriginalFilename() + " ya existe.");
        }

        // Guarda el nuevo archivo en el sistema de archivos local
        String filename = fileService.getUniqueFilename(file.getOriginalFilename());
        fileService.save(file, filename);

        // Actualiza la entidad con el nuevo archivo
        existingArchivo.setNombreArchivo(filename);
        existingArchivo.setRutaArchivo(fileService.getRutaArchivo(filename));

        // Actualiza otros campos si es necesario
        existingArchivo.setTipoArchivo(archivoDTO.getTipoArchivo());
        existingArchivo.setActividadContable(actividadContableRepository.findById(archivoDTO.getActividadContableId()).orElse(null));
        existingArchivo.setActividadLitigio(actividadLitigioRepository.findById(archivoDTO.getActividadLitigioId()).orElse(null));

        // Guarda los cambios
        Archivo updatedArchivo = archivoRepository.save(existingArchivo);

        return archivoMapper.toDTO(updatedArchivo);
    }

    @Override
    public ArchivoDTO updateArchivoMetadata(Long id, ArchivoDTO archivoDTO) {
        Archivo existingArchivo = archivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Archivo no encontrado con id: " + id));

        // Actualiza los metadatos del archivo
        if (archivoDTO.getTipoArchivo() != null) {
            existingArchivo.setTipoArchivo(archivoDTO.getTipoArchivo());
        }
        if (archivoDTO.getActividadContableId() != null) {
            ActividadContable actividadContable = actividadContableRepository.findById(archivoDTO.getActividadContableId()).orElse(null);
            existingArchivo.setActividadContable(actividadContable);
        }
        if (archivoDTO.getActividadLitigioId() != null) {
            ActividadLitigio actividadLitigio = actividadLitigioRepository.findById(archivoDTO.getActividadLitigioId()).orElse(null);
            existingArchivo.setActividadLitigio(actividadLitigio);
        }

        // Guarda los cambios
        Archivo updatedArchivo = archivoRepository.save(existingArchivo);

        return archivoMapper.toDTO(updatedArchivo);
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

    @Override
    public Resource exportActividadesToCSV() {
        List<ArchivoDTO> archivos = getAllArchivos();
        List<String> headers = List.of("ID", "NombreArchivo", "RutaArchivo", "TipoArchivo", "FechaCreacion", "ActividadContableId", "ActividadLitigioId");
        List<List<String>> data = archivos.stream()
                .map(archivo -> List.of(
                        archivo.getId().toString(),
                        archivo.getNombreArchivo().toString(),
                        archivo.getRutaArchivo().toString(),
                        archivo.getTipoArchivo().toString(),
                        archivo.getFechaCreacion().toString(),
                        archivo.getActividadContableId().toString(),
                        archivo.getActividadLitigioId().toString()

                ))
                .collect(Collectors.toList());

        if (data.isEmpty()) {
            logger.warn("No hay datos para exportar al CSV.");
        }

        return exportService.exportToCSV(headers, data);
    }
    @Override
    public Resource exportActividadesToPDF() {
        List<ArchivoDTO> archivos = getAllArchivos();
        List<String> headers = List.of("ID", "NombreArchivo", "RutaArchivo", "TipoArchivo", "FechaCreacion", "ActividadContableId", "ActividadLitigioId");
        List<List<String>> data = archivos.stream()
                .map(archivo -> List.of(
                        archivo.getId().toString(),
                        archivo.getNombreArchivo().toString(),
                        archivo.getRutaArchivo().toString(),
                        archivo.getTipoArchivo().toString(),
                        archivo.getFechaCreacion().toString(),
                        archivo.getActividadContableId().toString(),
                        archivo.getActividadLitigioId().toString()
                ))
                .collect(Collectors.toList());

        String title = "Reporte de Archivos";
        return exportService.exportToPDF(title, headers, data);
    }



    }
