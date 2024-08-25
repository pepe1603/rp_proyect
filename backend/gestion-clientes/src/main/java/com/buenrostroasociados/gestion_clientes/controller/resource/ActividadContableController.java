package com.buenrostroasociados.gestion_clientes.controller.resource;

import com.buenrostroasociados.gestion_clientes.dto.ActividadContableDTO;
import com.buenrostroasociados.gestion_clientes.service.ActividadContableService;
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
@RequestMapping("/api/v1/buenrostroAsociados/actividades-contables")
public class ActividadContableController {

    @Autowired
    private ActividadContableService actividadContableService;

    @GetMapping
    public ResponseEntity<List<ActividadContableDTO>> getAllActividadesContables() {
        List<ActividadContableDTO> actividades = actividadContableService.getAllActividadesContables();
        return new ResponseEntity<>(actividades, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ActividadContableDTO> createActividadContable(@Valid @RequestBody ActividadContableDTO actividadContableDTO) {
        ActividadContableDTO created = actividadContableService.saveActividadContable(actividadContableDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadContableDTO> getActividadContableById(@PathVariable Long id) {
        ActividadContableDTO actividad = actividadContableService.getActividadContableById(id);
        return new ResponseEntity<>(actividad, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActividadContableDTO> updateActividadContable(@PathVariable Long id, @Valid @RequestBody ActividadContableDTO actividadContableDTO) {
        ActividadContableDTO updated = actividadContableService.updateActividadContable(id, actividadContableDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActividadContable(@PathVariable Long id) {
        actividadContableService.deleteActividadContable(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/export/csv")
    public ResponseEntity<Resource> exportToCSV() {
        Resource resource = actividadContableService.exportActividadesToCSV();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = String.format("actividadesContables_%s.csv", timestamp);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .body(resource);
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<Resource> exportToPDF() {
        Resource resource = actividadContableService.exportActividadesToPDF();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = String.format("actividadesContables_%s.pdf", timestamp);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .body(resource);
    }

}

