package com.buenrostroasociados.gestion_clientes.service.email;

import com.buenrostroasociados.gestion_clientes.entity.Usuario;

public interface EmailService {


    void sendPasswordResetEmail(Usuario user, String token);

    void sendEmail(String to, String subject, String body);
}
