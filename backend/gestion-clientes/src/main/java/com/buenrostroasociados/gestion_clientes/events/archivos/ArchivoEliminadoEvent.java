package com.buenrostroasociados.gestion_clientes.events.archivos;

import org.springframework.context.ApplicationEvent;

public class ArchivoEliminadoEvent extends ApplicationEvent {

    private final String ArchivoTitle;

    public ArchivoEliminadoEvent(Object source, String archivoTitle) {
        super(source);
        ArchivoTitle = archivoTitle;
    }

    public String getArchivoTitle() {
        return ArchivoTitle;
    }
}
