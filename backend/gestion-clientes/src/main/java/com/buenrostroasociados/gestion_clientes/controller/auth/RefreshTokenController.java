package com.buenrostroasociados.gestion_clientes.controller.auth;

import com.buenrostroasociados.gestion_clientes.dto.auth.RefreshTokenRequest;
import com.buenrostroasociados.gestion_clientes.dto.auth.SigninResponse;
import com.buenrostroasociados.gestion_clientes.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class RefreshTokenController {

    @Autowired
    private AuthService authService;

    @PostMapping("/refresh-token")
    public ResponseEntity<SigninResponse> refreshAccessToken(@RequestBody String refreshToken) {
        SigninResponse response = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(response);
    }
}

