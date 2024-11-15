package com.buenrostroasociados.gestion_clientes.controller.publico;

import com.buenrostroasociados.gestion_clientes.dto.PublicInfoDTO;
import com.buenrostroasociados.gestion_clientes.service.publico.PublicInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/public")
public class PublicInfoController {

    @Autowired
    private final PublicInfoService publicInfoService;



    @GetMapping("/info")
    public ResponseEntity<PublicInfoDTO> obtenerInformacionPublica() {
        PublicInfoDTO info = publicInfoService.obtenerInformacionPublica();
        return ResponseEntity.ok(info);
    }
}
