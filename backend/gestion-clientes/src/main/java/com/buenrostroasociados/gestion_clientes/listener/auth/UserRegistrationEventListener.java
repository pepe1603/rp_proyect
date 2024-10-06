package com.buenrostroasociados.gestion_clientes.listener.auth;

import com.buenrostroasociados.gestion_clientes.events.auth.UserRegistrationEvent;
import com.buenrostroasociados.gestion_clientes.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationEventListener {

    @Autowired
    private NotificationService notificationService;

    @EventListener
    public void handleUserRegistrationEvent(UserRegistrationEvent event) {
        String subject = "Bienvenido a nuestra plataforma";
        String text = "Hola Estimado " + event.getUsername() + ",\n" +
                "Te has registrado exitosamente en nuestra plataforma Gestion de Actividades de Litigio y Contabilidad para Clientes. ¡Estamos emocionados de tenerte con nosotros!  :]" +
                "\nAhora puedes iniciar sesion en nuestra plataforma";

        notificationService.notifyEventUserRegister(event.getEmail(), subject, text);
    }
}
