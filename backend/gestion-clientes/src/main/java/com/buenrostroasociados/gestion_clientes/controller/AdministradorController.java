package com.buenrostroasociados.gestion_clientes.controller;

import com.buenrostroasociados.gestion_clientes.dto.AdministradorDTO;
import com.buenrostroasociados.gestion_clientes.service.AdministradorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @GetMapping
    public ResponseEntity<List<AdministradorDTO>> getAllAdministradores() {
        List<AdministradorDTO> administradores = administradorService.gatAllAdministradores();
        return new ResponseEntity<>(administradores, HttpStatus.OK);
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
}
