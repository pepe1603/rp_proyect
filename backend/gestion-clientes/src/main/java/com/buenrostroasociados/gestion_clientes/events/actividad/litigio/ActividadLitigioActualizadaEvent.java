package com.buenrostroasociados.gestion_clientes.events.actividad.litigio;

import org.springframework.context.ApplicationEvent;

public class ActividadLitigioActualizadaEvent extends ApplicationEvent {
    private final String actividadTitle;

    public ActividadLitigioActualizadaEvent(Object source, String actividadTitle) {
        super(source);
        this.actividadTitle = actividadTitle;
    }

    public String getActividadTitle() {
        return actividadTitle;
    }
}