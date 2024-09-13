package com.buenrostroasociados.gestion_clientes.exception;

public class EmailException extends RuntimeException{
    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
