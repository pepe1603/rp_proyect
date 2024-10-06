package com.buenrostroasociados.gestion_clientes.controller.auth;

import com.buenrostroasociados.gestion_clientes.dto.CustomErrorResponse;
import com.buenrostroasociados.gestion_clientes.dto.InfoResponse;
import com.buenrostroasociados.gestion_clientes.dto.auth.*;
import com.buenrostroasociados.gestion_clientes.exception.AccessDeniedException;
import com.buenrostroasociados.gestion_clientes.exception.TokenExpiredException;
import com.buenrostroasociados.gestion_clientes.exception.UnauthorizedException;
import com.buenrostroasociados.gestion_clientes.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Value("app-client.login-url")
    private String redirectUrlClient_login;
    @Value("app-client.reset-password-url")
    private String redirectUrlClient_forgotPassword;

    @Autowired
    private AuthService authService;


    @PostMapping("/sign_up")
    public ResponseEntity<InfoResponse> signUp(@Valid @RequestBody SignupRequest request) {
        authService.signUp(request);
        return ResponseEntity.ok(new InfoResponse("User registered successfully"));
    }

    @PostMapping("/sign_in")
    public ResponseEntity<?> signIn(@RequestBody SigninRequest request) {
        SigninResponse response = authService.signIn(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/password-reset-request")
    public ResponseEntity<?> requestPasswordReset(@RequestParam String email) {
        authService.sendPasswordResetLink(email);
        return ResponseEntity.ok(new InfoResponse("Password reset link sent to your email"));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<?> changePassword(@RequestParam String tokenReset, @RequestParam String newPassword) {

            authService.resetPassword(tokenReset, newPassword);
            InfoResponse response = new InfoResponse("Password successfully reset, Ahora puedes volver a iniciar sesion en el siguiente Link " + redirectUrlClient_login);
            return ResponseEntity.ok(response);


    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);                authService.logout(token);
                return ResponseEntity.ok(new InfoResponse("Logout successful"));
        }
        return ResponseEntity.badRequest().body(new InfoResponse("Invalid token"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<SigninResponse> refreshAccessToken(@RequestBody RefreshTokenRequest refreshToken) {
        SigninResponse response = authService.refreshAccessToken(refreshToken.getRefreshToken());
        return ResponseEntity.ok(response);
    }

}