package com.buenrostroasociados.gestion_clientes.controller;

import com.buenrostroasociados.gestion_clientes.dto.ActividadContableDTO;
import com.buenrostroasociados.gestion_clientes.service.ActividadContableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividades-contables")
public class ActividadContableController {

    @Autowired
    private ActividadContableService actividadContableService;

    @GetMapping
    public ResponseEntity<List<ActividadContableDTO>> getAllActividadesContables() {
        List<ActividadContableDTO> actividades = actividadContableService.getAllActividadesContables();
        return new ResponseEntity<>(actividades, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ActividadContableDTO> createActividadContable(@RequestBody ActividadContableDTO actividadContableDTO) {
        ActividadContableDTO created = actividadContableService.saveActividadContable(actividadContableDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadContableDTO> getActividadContableById(@PathVariable Long id) {
        ActividadContableDTO actividad = actividadContableService.getActividadContableById(id);
        return new ResponseEntity<>(actividad, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActividadContableDTO> updateActividadContable(@PathVariable Long id, @RequestBody ActividadContableDTO actividadContableDTO) {
        ActividadContableDTO updated = actividadContableService.updateActividadContable(id, actividadContableDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActividadContable(@PathVariable Long id) {
        actividadContableService.deleteActividadContable(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*
    @GetMapping("/{id}/archivos")
    public ResponseEntity<List<ArchivoDTO>> getArchivosByActividadContable(@PathVariable Long id) {
        List<ArchivoDTO> archivos = actividadContableService.getArchivosByActividadContableId(id);
        return new ResponseEntity<>(archivos, HttpStatus.OK);
    }*/
}

