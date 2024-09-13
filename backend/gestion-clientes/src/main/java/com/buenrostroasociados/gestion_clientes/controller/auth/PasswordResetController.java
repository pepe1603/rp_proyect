package com.buenrostroasociados.gestion_clientes.controller.auth;

import com.buenrostroasociados.gestion_clientes.dto.InfoResponse;
import com.buenrostroasociados.gestion_clientes.service.email.EmailService;
import com.buenrostroasociados.gestion_clientes.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/auth/public/rescue-account/")
public class PasswordResetController {
    private final AuthService authService;
    private final EmailService emailService;

    @Value("app-client.login-url")
    private String redirectUrlClient_login;
    @Value("app-client.reset-password-url")
    private String redirectUrlClient_forgotPassword;

    @Autowired
    public PasswordResetController(AuthService authService, EmailService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }

    @GetMapping("/password-reset/confirm")
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "/passwordReset/password-reset-confirm";//template para confirmar password-reset
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<?> confirmPasswordReset(@RequestParam String token, @RequestParam String newPassword, Model model) {
        try {
            authService.resetPassword(token, newPassword);
            model.addAttribute("message", "Password successfully reset. You can now log in.");
            model.addAttribute("redirectUrl", redirectUrlClient_login); // Usa la URL del login desde app.properties
            return ResponseEntity.ok(new InfoResponse("Password successfully reset. You can now log in."));
        } catch (Exception ex) {
            model.addAttribute("message", "Error resetting password: " + ex.getMessage());
            model.addAttribute("redirectUrl", redirectUrlClient_forgotPassword); // Usa la URL para volver a intentar
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InfoResponse("Error resetting password: " + ex.getMessage()));
        }
    }
}
