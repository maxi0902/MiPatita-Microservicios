package com.exampleMiPatita.demo.exception;

/**
 * Excepción de dominio que se lanza cuando las credenciales de login no son
 * válidas. El GlobalExceptionHandler la traduce a una respuesta 401 UNAUTHORIZED.
 */
public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
