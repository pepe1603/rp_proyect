package com.buenrostroasociados.gestion_clientes.exception;

public class JwtRefreshTokenException extends RuntimeException{
    private String message;

    public JwtRefreshTokenException(String message) {
        super(message);
    }
}
