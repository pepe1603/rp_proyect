package com.buenrostroasociados.gestion_clientes.events.actividad.contable;

import org.springframework.context.ApplicationEvent;

/**
 * Eventos específicos para actividades contables.
 */
public class ActividadContableCreadaEvent extends ApplicationEvent {
    private final String actividadTitle;

    public ActividadContableCreadaEvent(Object source, String actividadTitle) {
        super(source);
        this.actividadTitle = actividadTitle;
    }

    public String getActividadTitle() {
        return actividadTitle;
    }
}
