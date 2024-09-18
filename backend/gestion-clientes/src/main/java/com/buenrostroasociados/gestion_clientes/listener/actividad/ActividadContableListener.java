package com.buenrostroasociados.gestion_clientes.listener.actividad;


import com.buenrostroasociados.gestion_clientes.entity.Cliente;
import com.buenrostroasociados.gestion_clientes.events.actividad.contable.ActividadContableActualizadaEvent;
import com.buenrostroasociados.gestion_clientes.events.actividad.contable.ActividadContableCreadaEvent;
import com.buenrostroasociados.gestion_clientes.events.actividad.contable.ActividadContableEliminadaEvent;
import com.buenrostroasociados.gestion_clientes.notification.NotificationService;
import com.buenrostroasociados.gestion_clientes.service.ClienteService;
import com.buenrostroasociados.gestion_clientes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Listener para eventos de actividades contables.
 */
@Component
public class ActividadContableListener implements ApplicationListener<ApplicationEvent> {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ActividadContableCreadaEvent) {
            handleActividadContableCreada((ActividadContableCreadaEvent) event);
        } else if (event instanceof ActividadContableActualizadaEvent) {
            handleActividadContableActualizada((ActividadContableActualizadaEvent) event);
        } else if (event instanceof ActividadContableEliminadaEvent) {
            handleActividadContableEliminada((ActividadContableEliminadaEvent) event);
        }
    }

    private void handleActividadContableCreada(ActividadContableCreadaEvent event) {
        //notificar a los admins
        List<String> emails = usuarioService.getAllAdminEmails();
        for (String email : emails) {
            notificationService.notifyActivityContableCreation(email, event.getActividadTitle());
        }

    }

    private void handleActividadContableActualizada(ActividadContableActualizadaEvent event) {
        List<String> emails = usuarioService.getAllAdminEmails();
        for (String email : emails) {
            notificationService.notifyActivityContableUpdate(email, event.getActividadTitle());
        }
    }

    private void handleActividadContableEliminada(ActividadContableEliminadaEvent event) {
        List<String> emails = usuarioService.getAllAdminEmails();
        for (String email : emails) {
            notificationService.notifyActivityContableDeletion(email, event.getActividadTitle());
        }
    }

}
