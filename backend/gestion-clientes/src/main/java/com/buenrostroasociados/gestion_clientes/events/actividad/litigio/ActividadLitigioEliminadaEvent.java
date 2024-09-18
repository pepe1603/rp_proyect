package com.buenrostroasociados.gestion_clientes.events.actividad.litigio;

import org.springframework.context.ApplicationEvent;

public class ActividadLitigioEliminadaEvent extends ApplicationEvent {
    private final String actividadTitle;

    public ActividadLitigioEliminadaEvent(Object source, String actividadTitle) {
        super(source);
        this.actividadTitle = actividadTitle;
    }

    public String getActividadTitle() {
        return actividadTitle;
    }
}