package com.buenrostroasociados.gestion_clientes.controller.resource;

import com.buenrostroasociados.gestion_clientes.dto.ActividadLitigioDTO;
import com.buenrostroasociados.gestion_clientes.dto.InfoResponse;
import com.buenrostroasociados.gestion_clientes.entity.ActividadLitigio;
import com.buenrostroasociados.gestion_clientes.enums.EstadoCaso;
import com.buenrostroasociados.gestion_clientes.exception.BusinessException;
import com.buenrostroasociados.gestion_clientes.exception.UnauthorizedException;
import com.buenrostroasociados.gestion_clientes.service.ActividadLitigioService;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buenrostroAsociados/actividades-litigio")
public class ActividadLitigioController {

    @Autowired
    private ActividadLitigioService actividadLitigioService;

    @GetMapping
    public ResponseEntity<List<ActividadLitigioDTO>> getAllActividadesLitigio() {
        List<ActividadLitigioDTO> actividades = actividadLitigioService.getAllActividadesLitigio();
        return new ResponseEntity<>(actividades, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ActividadLitigioDTO> createActividadLitigio(@Valid @RequestBody ActividadLitigioDTO actividadLitigioDTO) {
        ActividadLitigioDTO created = actividadLitigioService.saveActividadLitigio(actividadLitigioDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadLitigioDTO> getActividadLitigioById(@PathVariable Long id) {
        ActividadLitigioDTO actividad = actividadLitigioService.getActividadLitigioById(id);
        return new ResponseEntity<>(actividad, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActividadLitigioDTO> updateActividadLitigio(@PathVariable Long id, @Valid @RequestBody ActividadLitigioDTO actividadLitigioDTO) {
        ActividadLitigioDTO updated = actividadLitigioService.updateActividadLitigio(id, actividadLitigioDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActividadLitigio(@PathVariable Long id) {
        actividadLitigioService.deleteActividadLitigio(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> updateEstadoCaso(@PathVariable Long id, @RequestParam String estadoCaso) {
        actividadLitigioService.updateEstadoActividadLitigio(id, estadoCaso);
        return new ResponseEntity<>(new InfoResponse("Estado Actualizado con exito"), HttpStatus.OK);
    }





}
