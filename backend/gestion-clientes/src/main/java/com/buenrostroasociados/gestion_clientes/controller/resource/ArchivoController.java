package com.buenrostroasociados.gestion_clientes.controller.resource;

import com.buenrostroasociados.gestion_clientes.dto.ArchivoDTO;
import com.buenrostroasociados.gestion_clientes.service.ArchivoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/v1/buenrostroAsociados/archivos")
public class ArchivoController {

    @Autowired
    private ArchivoService archivoService;

    @PostMapping
    public ResponseEntity<ArchivoDTO> uploadArchivo(
            @RequestParam("file") MultipartFile file,
            @Valid  @RequestParam("tipoArcchivo") String tipoArchivo,
            @RequestParam("actividadContableId") Long actividadContableId,
            @RequestParam("actividadLitigioId") Long actividadLitigioId,
            @RequestParam(value = "replaceExisting", defaultValue = "false") boolean replaceExisting) {

        // Crea el DTO del archivo sin usar la ID, ya que se maneja en el servicio
        ArchivoDTO archivoDTO = new ArchivoDTO();
        archivoDTO.setTipoArchivo(tipoArchivo);//
        archivoDTO.setActividadContableId(actividadContableId);
        archivoDTO.setActividadLitigioId(actividadLitigioId);

        ArchivoDTO savedArchivo = archivoService.saveArchivo(archivoDTO, file, replaceExisting);

        return new ResponseEntity<>(savedArchivo, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArchivoDTO> getArchivoById(@PathVariable Long id) {
        ArchivoDTO archivo = archivoService.getArchivo(id);
        return new ResponseEntity<>(archivo, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ArchivoDTO>> getAllArchivos() {
        List<ArchivoDTO> archivos = archivoService.getAllArchivos();
        return new ResponseEntity<>(archivos, HttpStatus.OK);
    }

    @GetMapping("/actividadLitigio/{id}")
    public ResponseEntity<List<ArchivoDTO>> getArchivosByActividadLitigio(@PathVariable Long id) {
        List<ArchivoDTO> archivos = archivoService.getArchivosByActividadLitigioId(id);
        return new ResponseEntity<>(archivos, HttpStatus.OK);
    }

    @GetMapping("/actividadContable/{id}")
    public ResponseEntity<List<ArchivoDTO>> getArchivosByActividadContable(@PathVariable Long id) {
        List<ArchivoDTO> archivos = archivoService.getArchivosByActividadContableId(id);
        return new ResponseEntity<>(archivos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArchivoDTO> updateArchivo(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "replaceExisting", defaultValue = "false") boolean replaceExisting) {

        // Si no se proporciona un archivo, solo actualiza los metadatos
        if (file == null || file.isEmpty()) {
            ArchivoDTO archivoDTO = archivoService.getArchivo(id);
            ArchivoDTO updatedArchivo = archivoService.updateArchivoMetadata(id, archivoDTO);
            return new ResponseEntity<>(updatedArchivo, HttpStatus.OK);
        } else {
            ArchivoDTO archivoDTO = new ArchivoDTO();
            archivoDTO.setNombreArchivo(file.getOriginalFilename());
            // Llama al servicio para reemplazar el archivo
            ArchivoDTO updatedArchivo = archivoService.updateArchivo(id, archivoDTO, file, replaceExisting);
            return new ResponseEntity<>(updatedArchivo, HttpStatus.OK);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ArchivoDTO> patchArchivo(
            @PathVariable Long id,
            @RequestBody ArchivoDTO archivoDTO) {

        ArchivoDTO updatedArchivo = archivoService.updateArchivoMetadata(id, archivoDTO);
        return new ResponseEntity<>(updatedArchivo, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArchivo(@PathVariable Long id) {
        archivoService.deleteArchivo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/export/csv")
    public ResponseEntity<Resource> exportToCSV() {
        Resource resource = archivoService.exportActividadesToCSV();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = String.format("archivos_%s.csv", timestamp);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .body(resource);
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<Resource> exportToPDF() {
        Resource resource = archivoService.exportActividadesToPDF();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = String.format("archivos_%s.pdf", timestamp);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .body(resource);
    }
}

/*
* Explicación

       POST /api/v1/buenrostroAsociados/archivos:
        Descripción: Carga un nuevo archivo.
        Parámetros: file, tipoArchivo, actividadContableId, actividadLitigioId, replaceExisting (opcional).
        Respuesta: 201 Created con el DTO del archivo guardado.

    GET /api/v1/buenrostroAsociados/archivos/{id}:
        Descripción: Obtiene un archivo por su ID.
        Respuesta: 200 OK con el DTO del archivo.

    GET /api/v1/buenrostroAsociados/archivos:
        Descripción: Obtiene todos los archivos.
        Respuesta: 200 OK con la lista de archivos.

    GET /api/v1/buenrostroAsociados/archivos/actividadContable/{id}:
        Descripción: Obtiene archivos por ID de actividad contable.
        Respuesta: 200 OK con la lista de archivos.

    GET /api/v1/buenrostroAsociados/archivos/actividadLitigio/{id}:
        Descripción: Obtiene archivos por ID de actividad de litigio.
        Respuesta: 200 OK con la lista de archivos.

    DELETE /api/v1/buenrostroAsociados/archivos/{id}:
        Descripción: Elimina un archivo por su ID.
        Respuesta: 204 No Content.

    PUT /api/v1/buenrostroAsociados/archivos/{id}:
        Descripción: Actualiza un archivo completo. Si se proporciona un nuevo archivo, lo reemplaza; si no, solo actualiza los metadatos.
        Parámetros: file (opcional), replaceExisting (opcional).
        Respuesta: 200 OK con el DTO del archivo actualizado.

    PATCH /api/v1/buenrostroAsociados/archivos/{id}:
        Descripción: Actualiza solo los metadatos del archivo.
        Parámetros: ArchivoDTO con los campos a actualizar.
        Respuesta: 200 OK con el DTO del archivo actualizado.

Nota

    En el caso de PUT, si no se proporciona un archivo nuevo (file es null o vacío), solo se actualizan los metadatos del archivo. De lo contrario, el archivo antiguo se reemplaza si replaceExisting es true.
    En el caso de PATCH, solo se actualizan los metadatos del archivo sin afectar el contenido del archivo.
* */