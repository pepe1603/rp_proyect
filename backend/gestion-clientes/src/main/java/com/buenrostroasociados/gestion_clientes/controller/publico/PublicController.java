package com.buenrostroasociados.gestion_clientes.controller.publico;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {
    @GetMapping("/info")
    public String getPublicInfo() {
        return "Información pública accesible sin autenticación.";
    }
}
