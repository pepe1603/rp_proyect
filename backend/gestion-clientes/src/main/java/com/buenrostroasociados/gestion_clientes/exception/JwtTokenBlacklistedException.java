package com.buenrostroasociados.gestion_clientes.exception;

public class JwtTokenBlacklistedException extends RuntimeException{
    public JwtTokenBlacklistedException(String message) {
        super(message);
    }
}
