package com.buenrostroasociados.gestion_clientes.listener.auth;

import com.buenrostroasociados.gestion_clientes.events.auth.UserLoginEvent;
import com.buenrostroasociados.gestion_clientes.events.auth.UserLogoutEvent;
import com.buenrostroasociados.gestion_clientes.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserLogoutEventListener {
    @Autowired
    private NotificationService notificationService;


    @EventListener
    public void handleUserLogoutEvent(UserLogoutEvent event) {

        String subject = "Notificación de Cierre de Sesión";
        String text ="Estimado/a " + event.getUsername() + ",\n\n" +
                "Se ha registrado un cierre de sesión en su cuenta el " + LocalDateTime.now() + ". ";

        notificationService.notifyEventUserLogout(event.getEmail(), subject, text);
    }

}
