package com.buenrostroasociados.gestion_clientes.listener.actividad;

import com.buenrostroasociados.gestion_clientes.events.actividad.contable.ActividadContableActualizadaEvent;
import com.buenrostroasociados.gestion_clientes.events.actividad.contable.ActividadContableCreadaEvent;
import com.buenrostroasociados.gestion_clientes.events.actividad.contable.ActividadContableEliminadaEvent;
import com.buenrostroasociados.gestion_clientes.events.actividad.litigio.ActividadLitigioActualizadaEvent;
import com.buenrostroasociados.gestion_clientes.events.actividad.litigio.ActividadLitigioCreadaEvent;
import com.buenrostroasociados.gestion_clientes.events.actividad.litigio.ActividadLitigioEliminadaEvent;
import com.buenrostroasociados.gestion_clientes.notification.NotificationService;
import com.buenrostroasociados.gestion_clientes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

/*
* Listener para evento de actividad Litigio
* * */
@Component
public class ActividadLitigioListener implements ApplicationListener<ApplicationEvent> {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ActividadLitigioCreadaEvent) {
            handleActividadLitigioCreada((ActividadLitigioCreadaEvent) event);
        } else if (event instanceof ActividadLitigioActualizadaEvent) {
            handleActividadLitigioActualizada((ActividadLitigioActualizadaEvent) event);
        } else if (event instanceof ActividadLitigioEliminadaEvent) {
            handleActividadLitigioEliminada((ActividadLitigioEliminadaEvent) event);
        }
    }

    private void handleActividadLitigioCreada(ActividadLitigioCreadaEvent event) {
        //notificar a los admins
        List<String> emails = usuarioService.getAllAdminEmails();
        for (String email : emails) {
            notificationService.notifyActivityLitigioCreation(email, event.getActividadTitle());
        }

    }

    private void handleActividadLitigioActualizada(ActividadLitigioActualizadaEvent event) {
        List<String> emails = usuarioService.getAllAdminEmails();
        for (String email : emails) {
            notificationService.notifyActivityLitigioUpdate(email, event.getActividadTitle());
        }
    }

    private void handleActividadLitigioEliminada(ActividadLitigioEliminadaEvent event) {
        List<String> emails = usuarioService.getAllAdminEmails();
        for (String email : emails) {
            notificationService.notifyActivityLitigioDeletion(email, event.getActividadTitle());
        }
    }
}
