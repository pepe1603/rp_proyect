package com.buenrostroasociados.gestion_clientes.listener.auth;

import com.buenrostroasociados.gestion_clientes.events.auth.PasswordConfirmationEvent;
import com.buenrostroasociados.gestion_clientes.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetEventListener implements ApplicationListener<ApplicationEvent>{
    private static final Logger loger = LoggerFactory.getLogger(PasswordConfirmationEvent.class);

    @Autowired
    private NotificationService notificationService;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof PasswordConfirmationEvent) {
            handlePasswordConfirmationEvent((PasswordConfirmationEvent) event);
        }
    }

    public void handlePasswordConfirmationEvent(PasswordConfirmationEvent event) {
        String subject = "Confirmación de Cambio de Contraseña";
        String text = "Hola mi estimado" + event.getUsername() + ",\n" +
                "Tu contraseña ha sido confirmada y actualizada exitosamente. ahora Puedes volver a iniciar sesion, si no fuite tu el que realizo el cambio, por favor ponte en contacto con soporte tecnico.";
        notificationService.notifyEventResetPassword(event.getEmail(), subject, text);
        loger.info("Event Listener HandleReset Password");
    }

}