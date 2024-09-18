package com.buenrostroasociados.gestion_clientes.controller.resource;

import com.buenrostroasociados.gestion_clientes.dto.UsuarioDTO;
import com.buenrostroasociados.gestion_clientes.dto.auth.SignupRequest;
import com.buenrostroasociados.gestion_clientes.service.UsuarioService;
import com.buenrostroasociados.gestion_clientes.service.auth.AuthService;
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
@RequestMapping("/api/v1/buenrostroAsociados/usuarios")
public class UsuarioController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> createUsuario(@Valid @RequestBody SignupRequest signupRequest){
        authService.signUp(signupRequest);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getUsuarios(){
        List<UsuarioDTO> usuarios = usuarioService.getAllUsuarios();
        return new ResponseEntity<>( usuarios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> createUsuario(@PathVariable Long id){
        UsuarioDTO usuario = usuarioService.getUsuarioById(id);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO){
        usuarioService.updateUsuario(id, usuarioDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}/username")
    public ResponseEntity<?> updateUsername(@PathVariable Long id, @RequestParam String newUsername){
        usuarioService.updateUsername(id, newUsername);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }


    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestParam String newPassword){
        usuarioService.updateUsername(id, newPassword);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id){
        usuarioService.deleteUsuario(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<Resource> exportToCSV() {
        Resource resource = usuarioService.exportUsuariosToCSV();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = String.format("usuarios_%s.csv", timestamp);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .body(resource);
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<Resource> exportToPDF() {
        Resource resource = usuarioService.exportUsuariosToPDF();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = String.format("usuarios_%s.pdf", timestamp);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .body(resource);
    }






}
