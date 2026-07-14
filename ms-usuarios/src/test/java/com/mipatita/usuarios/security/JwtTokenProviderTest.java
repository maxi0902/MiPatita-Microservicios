package com.mipatita.usuarios.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del proveedor de tokens JWT.
 * Verifica el ciclo completo: crear un token, validarlo y extraer el usuario.
 */
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        // secreto de al menos 32 caracteres (requerido por HS256/HS384)
        String secret = "ClaveSecretaDePruebaParaJwtMiPatita2026SuperSegura";
        jwtTokenProvider = new JwtTokenProvider(secret, 3600000L);
    }

    @Test
    void createToken_y_getUsername_devuelveElMismoUsuario() {
        String token = jwtTokenProvider.createToken("maxito");
        assertEquals("maxito", jwtTokenProvider.getUsernameFromToken(token));
    }

    @Test
    void validateToken_tokenGenerado_esValido() {
        String token = jwtTokenProvider.createToken("maxito");
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_tokenBasura_esInvalido() {
        assertFalse(jwtTokenProvider.validateToken("esto.no.es.un.token"));
    }
}
