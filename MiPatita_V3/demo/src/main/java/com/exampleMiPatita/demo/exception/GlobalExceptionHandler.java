package com.exampleMiPatita.demo.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejo CENTRALIZADO de excepciones para toda la aplicación.
 *
 * En lugar de poner try/catch en cada controlador, capturamos las excepciones
 * en un solo lugar y devolvemos un código HTTP semántico coherente:
 *  - 400 BAD REQUEST  -> errores de validación (@Valid)
 *  - 401 UNAUTHORIZED -> credenciales inválidas
 *  - 500 INTERNAL...  -> cualquier error inesperado (con log para depurar)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 400 -> Se dispara cuando @Valid detecta campos inválidos en el body
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErroresValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
        });
        log.warn("Petición rechazada por validación: {}", errores);
        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }

    // 401 -> Credenciales inválidas en el login
    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<Map<String, String>> manejarCredencialesInvalidas(CredencialesInvalidasException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    // 500 -> Red de seguridad para cualquier error no contemplado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> manejarErrorGeneral(Exception ex) {
        log.error("Error inesperado en la aplicación: {}", ex.getMessage(), ex);
        Map<String, String> error = new HashMap<>();
        error.put("error", "Ocurrió un error interno en el servidor");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
