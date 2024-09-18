package com.buenrostroasociados.gestion_clientes.service.email;

public interface EmailService {


    void sendPasswordResetEmail(String to, String token);

    void sendEmail(String to, String subject, String body);
}
