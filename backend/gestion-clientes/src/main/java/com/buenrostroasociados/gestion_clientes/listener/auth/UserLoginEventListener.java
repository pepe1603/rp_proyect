package com.buenrostroasociados.gestion_clientes.listener.auth;

import com.buenrostroasociados.gestion_clientes.events.auth.UserLoginEvent;
import com.buenrostroasociados.gestion_clientes.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserLoginEventListener {
    @Autowired
    private NotificationService notificationService;


    @EventListener
    public void handleUserLoginEvent(UserLoginEvent event) {

        String subject = "Notificación de Inicio de Sesión";
        String text = "Hola estimado/a " + event.getUsername() + ",\n\n "+
                "Se ha detectado un inicio de sesión en tu cuenta el "+ LocalDateTime.now() +".\n\n";

        notificationService.notifyEventUserLogin(event.getEmail(), subject, text);
    }

}
