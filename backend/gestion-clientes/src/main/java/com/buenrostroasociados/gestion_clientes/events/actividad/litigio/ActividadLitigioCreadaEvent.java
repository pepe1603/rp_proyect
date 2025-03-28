package com.buenrostroasociados.gestion_clientes.events.actividad.litigio;

import org.springframework.context.ApplicationEvent;

/**
 * Eventos específicos para actividades litigiosas.
 */
public class ActividadLitigioCreadaEvent extends ApplicationEvent {
    private final String actividadTitle;

    public ActividadLitigioCreadaEvent(Object source, String actividadTitle) {
        super(source);
        this.actividadTitle = actividadTitle;
    }

    public String getActividadTitle() {
        return actividadTitle;
    }
}