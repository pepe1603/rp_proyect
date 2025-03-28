package com.buenrostroasociados.gestion_clientes.events.auth;

import org.springframework.context.ApplicationEvent;

public class UserLoginEvent extends ApplicationEvent {
    private final String username;
    private final String email;

    public UserLoginEvent(Object source, String username, String email) {
        super(source);
        this.username = username;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
