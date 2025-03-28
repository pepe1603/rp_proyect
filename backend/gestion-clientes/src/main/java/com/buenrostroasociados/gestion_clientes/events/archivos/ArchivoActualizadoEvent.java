package com.buenrostroasociados.gestion_clientes.events.archivos;

import org.springframework.context.ApplicationEvent;

public class ArchivoActualizadoEvent extends ApplicationEvent {

    private final String ArchivoTitle;

    public ArchivoActualizadoEvent(Object source, String archivoTitle) {
        super(source);
        ArchivoTitle = archivoTitle;
    }

    public String getArchivoTitle() {
        return ArchivoTitle;
    }
}
