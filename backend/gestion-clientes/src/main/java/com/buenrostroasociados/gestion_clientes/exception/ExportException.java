package com.buenrostroasociados.gestion_clientes.exception;

public class ExportException extends RuntimeException{
    public ExportException(String message) {
        super(message);
    }

    public ExportException(String message, Throwable cause) {
        super(message, cause);
    }
}
