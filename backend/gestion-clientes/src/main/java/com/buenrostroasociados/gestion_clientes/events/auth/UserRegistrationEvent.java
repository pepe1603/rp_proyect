package com.buenrostroasociados.gestion_clientes.events.auth;

import org.springframework.context.ApplicationEvent;

public class UserRegistrationEvent extends ApplicationEvent {
    private final  String username;
    private final String email;

    public UserRegistrationEvent(Object source, String username, String email) {
        super(source);
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}