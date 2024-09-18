package com.buenrostroasociados.gestion_clientes.listener.archivo;

import com.buenrostroasociados.gestion_clientes.events.archivos.ArchivoActualizadoEvent;
import com.buenrostroasociados.gestion_clientes.events.archivos.ArchivoCreadoEvent;
import com.buenrostroasociados.gestion_clientes.events.archivos.ArchivoEliminadoEvent;
import com.buenrostroasociados.gestion_clientes.notification.NotificationService;
import com.buenrostroasociados.gestion_clientes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArchivoListener implements ApplicationListener<ApplicationEvent> {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ArchivoCreadoEvent) {
            handleArchivoCreado((ArchivoCreadoEvent) event);
        } else if (event instanceof ArchivoActualizadoEvent) {
            handleArchivoActualizado((ArchivoActualizadoEvent) event);
        } else if (event instanceof ArchivoEliminadoEvent) {
            handleArchivoEliminado((ArchivoEliminadoEvent) event);
        }
    }

    private void handleArchivoCreado(ArchivoCreadoEvent event) {
        //notificar a los admins
        List<String> emails = usuarioService.getAllAdminEmails();
        for (String email : emails) {
            notificationService.notifyArchivoCreation(email, event.getArchivoTitle());
        }

    }

    private void handleArchivoActualizado(ArchivoActualizadoEvent event) {
        List<String> emails = usuarioService.getAllAdminEmails();
        for (String email : emails) {
            notificationService.notifyArchivoUpdate(email, event.getArchivoTitle());
        }
    }

    private void handleArchivoEliminado(ArchivoEliminadoEvent event) {
        List<String> emails = usuarioService.getAllAdminEmails();
        for (String email : emails) {
            notificationService.notifyArchivoDeletion(email, event.getArchivoTitle());
        }
    }
}
