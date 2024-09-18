package com.buenrostroasociados.gestion_clientes.events.actividad.contable;


import org.springframework.context.ApplicationEvent;

public class ActividadContableEliminadaEvent extends ApplicationEvent {
    private final String actividadTitle;

    public ActividadContableEliminadaEvent(Object source, String actividadTitle) {
        super(source);
        this.actividadTitle = actividadTitle;
    }

    public String getActividadTitle() {
        return actividadTitle;
    }
}