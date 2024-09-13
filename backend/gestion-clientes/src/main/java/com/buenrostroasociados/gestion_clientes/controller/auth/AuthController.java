package com.buenrostroasociados.gestion_clientes.controller.auth;

import com.buenrostroasociados.gestion_clientes.dto.InfoResponse;
import com.buenrostroasociados.gestion_clientes.dto.auth.PasswordResetRequest;
import com.buenrostroasociados.gestion_clientes.dto.auth.SigninRequest;
import com.buenrostroasociados.gestion_clientes.dto.auth.SigninResponse;
import com.buenrostroasociados.gestion_clientes.dto.auth.SignupRequest;
import com.buenrostroasociados.gestion_clientes.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/sign_up")
    public ResponseEntity<?> signUp(@RequestBody SignupRequest request) {
        authService.signUp(request);
        return ResponseEntity.ok(new InfoResponse("User registered successfully"));
    }

    @PostMapping("/sign_in")
    public ResponseEntity<?> signIn(@RequestBody SigninRequest request) {
        SigninResponse response = authService.signIn(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/password-reset-request")
    public ResponseEntity<?> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        authService.sendPasswordResetLink(request.getEmail());
        return ResponseEntity.ok(new InfoResponse("Password reset link sent"));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody PasswordResetRequest request) {
        authService.resetPassword(token, request.getNewPassword());
        return ResponseEntity.ok(new InfoResponse("Password successfully reset"));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                authService.logout(token);
                return ResponseEntity.ok(new InfoResponse("Logout successful"));
            } catch (Exception ex) {
                return ResponseEntity.status(500).body(new InfoResponse("Error during logout"));
            }
        }
        return ResponseEntity.badRequest().body(new InfoResponse("Invalid token"));
    }


}