package com.buenrostroasociados.gestion_clientes.controller.resource;

import com.buenrostroasociados.gestion_clientes.dto.AdministradorDTO;
import com.buenrostroasociados.gestion_clientes.dto.CustomErrorResponse;
import com.buenrostroasociados.gestion_clientes.exception.EntityNotFoundException;
import com.buenrostroasociados.gestion_clientes.service.AdministradorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/v1/buenrostroAsociados/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @GetMapping("/hola")
    public String hello(){
        return "HOla munod Spring Boot";
    }

    @GetMapping
    public ResponseEntity<?> getAllAdministradores() {
        try {
            List<AdministradorDTO> administradores = administradorService.getAllAdministradores();
            return new ResponseEntity<>(administradores, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found Entity", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "Ocurrio un error inesperado: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping
    public ResponseEntity<AdministradorDTO> createAdministrador(@Valid @RequestBody AdministradorDTO administradorDTO) {
        AdministradorDTO created = administradorService.savedAdministrador(administradorDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministradorDTO> getAdministradorById(@PathVariable Long id) {
        AdministradorDTO administrador = administradorService.getAdministradorById(id);
        return new ResponseEntity<>(administrador, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministradorDTO> updateAdministrador(@PathVariable Long id,@Valid @RequestBody AdministradorDTO administradorDTO) {
        AdministradorDTO updated = administradorService.updateAdministrador(id, administradorDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrador(@PathVariable Long id) {
        administradorService.deleteAdministrador(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<AdministradorDTO> getAdministradorByEmail(@PathVariable String email) {
        AdministradorDTO administrador = administradorService.getAdministradorByEmail(email);
        return new ResponseEntity<>(administrador, HttpStatus.OK);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<Resource> exportToCSV() {
        Resource resource = administradorService.exportActividadesToCSV();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = String.format("adminstradores_%s.csv", timestamp);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .body(resource);
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<Resource> exportToPDF() {
        Resource resource = administradorService.exportActividadesToPDF();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = String.format("administradores_%s.pdf", timestamp);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .body(resource);
    }

}
