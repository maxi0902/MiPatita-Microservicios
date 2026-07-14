package com.mipatita.usuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mipatita.usuarios.exception.CredencialesInvalidasException;
import com.mipatita.usuarios.model.Usuario;
import com.mipatita.usuarios.repository.UsuarioRepository;
import com.mipatita.usuarios.security.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioGuardado() {
        Usuario u = new Usuario();
        u.setId(1);
        u.setUsername("maxito");
        u.setPassword("hashBcrypt");
        u.setRole("ADMIN");
        return u;
    }

    @Test
    void registrarUsuario_nuevo_cifraPasswordYGuarda() {
        Usuario nuevo = new Usuario();
        nuevo.setUsername("maxito");
        nuevo.setPassword("1234");
        when(usuarioRepository.findByUsername("maxito")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("hashBcrypt");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario guardado = usuarioService.registrarUsuario(nuevo);

        assertEquals("hashBcrypt", guardado.getPassword());
        verify(usuarioRepository).save(nuevo);
    }

    @Test
    void registrarUsuario_conUsernameExistente_lanzaExcepcion() {
        Usuario nuevo = new Usuario();
        nuevo.setUsername("maxito");
        nuevo.setPassword("1234");
        when(usuarioRepository.findByUsername("maxito")).thenReturn(Optional.of(usuarioGuardado()));

        assertThrows(IllegalArgumentException.class, () -> usuarioService.registrarUsuario(nuevo));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void autenticar_credencialesCorrectas_devuelveToken() {
        when(usuarioRepository.findByUsername("maxito")).thenReturn(Optional.of(usuarioGuardado()));
        when(passwordEncoder.matches("1234", "hashBcrypt")).thenReturn(true);
        when(jwtTokenProvider.createToken("maxito")).thenReturn("token.jwt.generado");

        String token = usuarioService.autenticar("maxito", "1234");

        assertEquals("token.jwt.generado", token);
    }

    @Test
    void autenticar_usuarioInexistente_lanza401() {
        when(usuarioRepository.findByUsername("fantasma")).thenReturn(Optional.empty());
        assertThrows(CredencialesInvalidasException.class,
                () -> usuarioService.autenticar("fantasma", "1234"));
    }

    @Test
    void autenticar_passwordIncorrecta_lanza401() {
        when(usuarioRepository.findByUsername("maxito")).thenReturn(Optional.of(usuarioGuardado()));
        when(passwordEncoder.matches("malaclave", "hashBcrypt")).thenReturn(false);
        assertThrows(CredencialesInvalidasException.class,
                () -> usuarioService.autenticar("maxito", "malaclave"));
        verify(jwtTokenProvider, never()).createToken(anyString());
    }

    @Test
    void validarToken_delegaEnElProvider() {
        when(jwtTokenProvider.validateToken("token.valido")).thenReturn(true);
        assertTrue(usuarioService.validarToken("token.valido"));
    }
}
