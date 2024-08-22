package com.buenrostroasociados.gestion_clientes.controller;

import com.buenrostroasociados.gestion_clientes.dto.ArchivoDTO;
import com.buenrostroasociados.gestion_clientes.service.ArchivoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/archivos")
public class ArchivoController {

    @Autowired
    private ArchivoService archivoService;

    @PostMapping
    public ResponseEntity<ArchivoDTO> uploadArchivo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("actividadContableId") Long actividadContableId,
            @RequestParam("actividadLitigioId") Long actividadLitigioId,
            @RequestParam(value = "replaceExisting", defaultValue = "false") boolean replaceExisting) {

        // Crea el DTO del archivo sin usar la ID, ya que se maneja en el servicio
        ArchivoDTO archivoDTO = new ArchivoDTO();
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArchivo(@PathVariable Long id) {
        archivoService.deleteArchivo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

/*
* Explicación

    uploadArchivo:
        Este método maneja las solicitudes POST para cargar archivos. Recibe el archivo como un MultipartFile y los IDs opcionales de actividadContable y actividadLitigio.
        El parámetro replaceExisting se usa para decidir si reemplazar un archivo existente con el mismo nombre.
        Llama al método saveArchivo del servicio, que maneja la lógica de almacenamiento y reemplazo de archivos.

    getArchivoById:
        Recupera un archivo por su ID. Llama al método getArchivo del servicio y devuelve el archivo como ArchivoDTO.

    getAllArchivos:
        Recupera todos los archivos en el sistema. Llama al método getAllArchivos del servicio y devuelve la lista de archivos como ArchivoDTO.

    deleteArchivo:
        Elimina un archivo por su ID. Llama al método deleteArchivo del servicio y elimina tanto el archivo del sistema de archivos como el registro de la base de datos.
*
* */