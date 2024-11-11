package com.buenrostroasociados.gestion_clientes.service.impl;

import com.buenrostroasociados.gestion_clientes.dto.ArchivoDTO;
import com.buenrostroasociados.gestion_clientes.entity.ActividadContable;
import com.buenrostroasociados.gestion_clientes.entity.ActividadLitigio;
import com.buenrostroasociados.gestion_clientes.entity.Archivo;
import com.buenrostroasociados.gestion_clientes.enums.ClaseArchivo;
import com.buenrostroasociados.gestion_clientes.events.archivos.ArchivoActualizadoEvent;
import com.buenrostroasociados.gestion_clientes.events.archivos.ArchivoEliminadoEvent;
import com.buenrostroasociados.gestion_clientes.exception.ActividadConflictException;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.exception.InvalidFileTypeException;
import com.buenrostroasociados.gestion_clientes.exception.ResourceNotFoundException;
import com.buenrostroasociados.gestion_clientes.mapper.ArchivoMapper;
import com.buenrostroasociados.gestion_clientes.notification.NotificationService;
import com.buenrostroasociados.gestion_clientes.repository.ActividadContableRepository;
import com.buenrostroasociados.gestion_clientes.repository.ActividadLitigioRepository;
import com.buenrostroasociados.gestion_clientes.repository.ArchivoRepository;
import com.buenrostroasociados.gestion_clientes.service.ArchivoService;
import com.buenrostroasociados.gestion_clientes.service.awss3.S3Service;
import com.buenrostroasociados.gestion_clientes.service.export.ExportService;
import com.buenrostroasociados.gestion_clientes.utils.CurrentUserAuthenticated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private S3Service s3Service;

    @Autowired
    private ArchivoMapper archivoMapper;

    @Autowired
    private ExportService exportService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    @Override
    public ArchivoDTO saveArchivo(ArchivoDTO archivoDTO, MultipartFile file, boolean replaceExisting) {
        // Verificar que solo un tipo de actividad esté presente (Contable o Litigio)
        if (archivoDTO.getActividadContableId() != null && archivoDTO.getActividadLitigioId() != null) {
            logger.error("Intento de asignar tanto actividad contable como litigio al archivo.");
            throw new ActividadConflictException("No se puede asignar ambas actividades al mismo archivo.");
        }

        // Obtener las entidades correspondientes si se especifican
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

        archivoDTO.setFechaCreacion(LocalDateTime.now());  // Establece la fecha de creación como la fecha actual

        // Verificar si el archivo ya existe en la base de datos
        Optional<Archivo> existingArchivo = archivoRepository.findByNombreArchivo(file.getOriginalFilename());

        // Si el archivo ya existe en la base de datos y en S3
        if (existingArchivo.isPresent()) {
            if (replaceExisting) {
                // Si se debe reemplazar, eliminamos el archivo de S3 y de la base de datos
                logger.info("Reemplazando archivo existente: {}", file.getOriginalFilename());
                s3Service.deleteFile(existingArchivo.get().getRutaArchivo());  // Eliminar el archivo de S3
                archivoRepository.delete(existingArchivo.get());  // Eliminar de la base de datos
            } else {
                // Si no se reemplaza, se retorna un mensaje de advertencia
                logger.warn("El archivo {} ya existe y no se reemplazará.", file.getOriginalFilename());
                return existingArchivo.map(archivoMapper::toDTO).orElseThrow();
            }
        } else {
            logger.info("Archivo no encontrado en la base de datos. Procediendo con la carga.");
        }

        // Validar el tipo de archivo antes de proceder (si es necesario)
        validateFileType(file);

        // Subir el archivo a S3
        String fileName = file.getOriginalFilename();
        try {
            logger.info("Subiendo archivo a S3: {}", fileName);
            s3Service.uploadFile(fileName, file.getResource());  // Subimos el archivo a S3
        } catch (Exception e) {
            logger.error("Error al subir el archivo a S3: {}", fileName, e);
            throw new RuntimeException("Error al subir el archivo a S3", e);
        }

        // Crear el objeto Archivo en la base de datos
        Archivo archivo = archivoMapper.toEntity(archivoDTO);
        archivo.setRutaArchivo(fileName);  // Guardamos solo la clave (nombre del archivo) en la base de datos
        archivo.setActividadContable(actividadContable);
        archivo.setActividadLitigio(actividadLitigio);

        // Guardamos el archivo en la base de datos
        archivoRepository.save(archivo);

        logger.info("Archivo guardado correctamente en la base de datos con la clave S3: {}", fileName);

        // Publicar el evento de actualización
        eventPublisher.publishEvent(new ArchivoActualizadoEvent(this, archivo.getNombreArchivo()));
        notificationService.notifyArchivoCreation(CurrentUserAuthenticated.getEmailUserRolClient(), archivo.getNombreArchivo());


        // Retornamos el DTO del archivo guardado
        return archivoMapper.toDTO(archivo);
    }



    @Override
    public ArchivoDTO getArchivo(Long id) {
        logger.info("Buscando archivo con id: {}", id);
        Archivo archivo = archivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Archivo no encontrado con id: " + id));

        logger.info("Archivo encontrado: {}", archivo.getNombreArchivo());
        return archivoMapper.toDTO(archivo);
    }


    /* Nota: considerar
    Optimización de Búsquedas: Si tu base de datos crece mucho, podrías pensar en implementar
    paginación o filtrado de archivos para evitar que la carga de todos los archivos a la vez cause problemas de rendimiento.
     */
    @Override
    public List<ArchivoDTO> getAllArchivos() {
        List<Archivo> archivos = archivoRepository.findAll();

        if (archivos.isEmpty()) {
            logger.warn("No se encontraron archivos en el repositorio.");
            throw new EntityNotFoundException("No se encontró ningún registro de archivos en el repositorio.");
        }

        logger.info("Se encontraron {} archivos.", archivos.size());
        return archivos.stream()
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
        // Buscar archivo existente
        Archivo existingArchivo = archivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Archivo no encontrado con id: " + id));

        // Log para mostrar que se está actualizando un archivo
        logger.info("Iniciando actualización del archivo con id: {} y nombre: {}", id, existingArchivo.getNombreArchivo());

        // Verifica si el archivo existe en la base de datos y si se debe reemplazar
        Optional<Archivo> oldArchivo = archivoRepository.findByNombreArchivo(file.getOriginalFilename());

        if (oldArchivo.isPresent()) {
            if (replaceExisting) {
                // Eliminar archivo viejo de S3
                logger.info("Reemplazando archivo existente: {}", file.getOriginalFilename());
                s3Service.deleteFile(oldArchivo.get().getRutaArchivo()); // Eliminar de S3

                // Eliminar el registro viejo en la base de datos
                archivoRepository.delete(oldArchivo.get());
            } else {
                // Si no se reemplaza el archivo, lanzar un warn
                logger.warn("El archivo con nombre {} ya existe y no se reemplazará.", file.getOriginalFilename());
                return archivoMapper.toDTO(oldArchivo.get());//retornamos el archivo existente
            }
        }

        // Guardar el nuevo archivo en el sistema de archivos (S3 en este caso)
        String filename = file.getOriginalFilename();  // Usar el nombre original del archivo (puedes cambiar esto si es necesario)
        try {
            logger.info("Subiendo archivo a S3: {}", filename);
            s3Service.uploadFile(filename, file.getResource());
        } catch (Exception e) {
            logger.error("Error al subir el archivo a S3: {}", filename, e);
            throw new RuntimeException("Error al subir el archivo a S3", e);
        }

        // Actualizar los datos del archivo
        existingArchivo.setNombreArchivo(filename); // Nombre actualizado del archivo
        existingArchivo.setRutaArchivo(filename);   // Ruta en S3 (usamos el nombre del archivo como clave)

        // Validar y actualizar el tipo de archivo
        String tipoArchivoFormated = archivoDTO.getTipoArchivo().toLowerCase();
        ClaseArchivo newTipoArchivo;
        try {
            newTipoArchivo = ClaseArchivo.valueOf(tipoArchivoFormated);
        } catch (ActividadConflictException ex) {
            logger.error("Tipo de archivo no válido: {}", archivoDTO.getTipoArchivo());
            throw new ActividadConflictException("Tipo de archivo no válido. Los tipos válidos son: [LITIGIO, CONTABLE, NO_ESPECIFICADO].");
        }

        existingArchivo.setTipoArchivo(newTipoArchivo);

        // Asociar las actividades (si existen)
        if (archivoDTO.getActividadContableId() != null) {
            ActividadContable actividadContable = actividadContableRepository.findById(archivoDTO.getActividadContableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Actividad Contable no encontrada con id: " + archivoDTO.getActividadContableId()));
            existingArchivo.setActividadContable(actividadContable);
        } else {
            existingArchivo.setActividadContable(null); // No asociar si no se encuentra el ID
        }

        if (archivoDTO.getActividadLitigioId() != null) {
            ActividadLitigio actividadLitigio = actividadLitigioRepository.findById(archivoDTO.getActividadLitigioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Actividad Litigio no encontrada con id: " + archivoDTO.getActividadLitigioId()));
            existingArchivo.setActividadLitigio(actividadLitigio);
        } else {
            existingArchivo.setActividadLitigio(null); // No asociar si no se encuentra el ID
        }

        // Guardar los cambios en la base de datos
        Archivo updatedArchivo = archivoRepository.save(existingArchivo);

        // Publicar el evento de actualización
        eventPublisher.publishEvent(new ArchivoActualizadoEvent(this, updatedArchivo.getNombreArchivo()));
        notificationService.notifyArchivoUpdate(CurrentUserAuthenticated.getEmailUserRolClient(), updatedArchivo.getNombreArchivo());

        logger.info("Archivo con id: {} actualizado exitosamente. Nuevo nombre de archivo: {}", id, updatedArchivo.getNombreArchivo());

        // Retornar el DTO del archivo actualizado
        return archivoMapper.toDTO(updatedArchivo);
    }


    @Transactional
    @Override
    public ArchivoDTO updateArchivoMetadata(Long id, ArchivoDTO archivoDTO) {
        // Busca el archivo existente
        Archivo existingArchivo = archivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Archivo no encontrado con id: " + id));

        // Actualiza los metadatos del archivo
        if (archivoDTO.getTipoArchivo() != null) {
            // Validar y asignar el tipo de archivo utilizando el nuevo método
            ClaseArchivo newTipoArchivo = validateAndAssignArchivoType(archivoDTO.getTipoArchivo());
            existingArchivo.setTipoArchivo(newTipoArchivo);
        }

        // Actualiza la actividad contable si es necesario
        if (archivoDTO.getActividadContableId() != null) {
            ActividadContable actividadContable = actividadContableRepository.findById(archivoDTO.getActividadContableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Actividad Contable no encontrada con id: " + archivoDTO.getActividadContableId()));
            existingArchivo.setActividadContable(actividadContable);
        }

        // Actualiza la actividad de litigio si es necesario
        if (archivoDTO.getActividadLitigioId() != null) {
            ActividadLitigio actividadLitigio = actividadLitigioRepository.findById(archivoDTO.getActividadLitigioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Actividad Litigio no encontrada con id: " + archivoDTO.getActividadLitigioId()));
            existingArchivo.setActividadLitigio(actividadLitigio);
        }

        // Guarda los cambios en la base de datos
        Archivo updatedArchivo = archivoRepository.save(existingArchivo);

        // Publicar evento de actualización
        eventPublisher.publishEvent(new ArchivoActualizadoEvent(this, updatedArchivo.getNombreArchivo()));
        notificationService.notifyArchivoUpdate(CurrentUserAuthenticated.getEmailUserRolClient(), updatedArchivo.getNombreArchivo());

        // Log de éxito
        logger.info("Archivo con ID {} actualizado con éxito.", id);

        // Retorna el DTO actualizado
        return archivoMapper.toDTO(updatedArchivo);
    }



    @Override
    public void deleteArchivo(Long id) {
        // Buscar el archivo en la base de datos
        Archivo archivo = archivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El archivo no pudo ser eliminado porque no se encontró el archivo con el ID: " + id));

        try {
            // Verifica si el archivo existe en S3
            if (!s3Service.fileExists(archivo.getRutaArchivo())) {
                logger.warn("El archivo con nombre {} no se encontró en S3.", archivo.getRutaArchivo());
            } else {
                // Elimina el archivo de S3
                s3Service.deleteFile(archivo.getRutaArchivo());
                logger.info("Archivo con nombre {} eliminado de S3.", archivo.getRutaArchivo());
            }
        } catch (Exception e) {
            logger.error("Error al intentar eliminar el archivo de S3: {}", archivo.getRutaArchivo(), e);
            // Lanza una excepción si la eliminación del archivo falla en S3
            throw new RuntimeException("Error al eliminar el archivo de S3.", e);
        }

        // Elimina el registro en la base de datos
        archivoRepository.delete(archivo);
        logger.info("Archivo con ID {} eliminado de la base de datos.", id);

        // Publicar evento de eliminación
        eventPublisher.publishEvent(new ArchivoEliminadoEvent(this, archivo.getRutaArchivo()));
        notificationService.notifyArchivoDeletion(CurrentUserAuthenticated.getEmailUserRolClient(), archivo.getRutaArchivo());
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

///------------ Metohotds Aux

    /**
     * Valida y asigna el tipo de archivo al archivo proporcionado.
     * Este método verifica que el tipo de archivo esté entre los valores válidos.
     *
     * @param tipoArchivo El tipo de archivo a validar y asignar.
     * @return El tipo de archivo validado (ClaseArchivo).
     */
    private ClaseArchivo validateAndAssignArchivoType(String tipoArchivo) {
        try {
            // Convertir el tipo a mayúsculas y luego asignar el valor correspondiente
            String tipoArchivoFormated = tipoArchivo.toUpperCase();
            return ClaseArchivo.valueOf(tipoArchivoFormated);
        } catch (IllegalArgumentException ex) {
            // Si el tipo no es válido, se lanza una excepción
            logger.error("Tipo de archivo no válido: {}", tipoArchivo);
            throw new IllegalArgumentException("Tipo de archivo no válido. Debe ser uno de los siguientes: LITIGIO, CONTABLE, NO_ESPECIFICADO.");
        }
    }


    /**
     * Valida el tipo de archivo (por ejemplo, solo PDF).
     */
    private void validateFileType(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
            logger.error("Tipo de archivo no permitido: {}", fileName);
            throw new InvalidFileTypeException("Solo se permiten archivos PDF.");
        }
    }


    }
