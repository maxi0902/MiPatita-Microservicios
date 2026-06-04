package com.exampleMiPatita.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exampleMiPatita.demo.exception.CredencialesInvalidasException;
import com.exampleMiPatita.demo.model.Usuario;
import com.exampleMiPatita.demo.repository.UsuarioRepository;
import com.exampleMiPatita.demo.security.JwtTokenProvider;

/**
 * Capa de SERVICIO de usuarios.
 *
 * Aquí vive TODA la lógica de negocio relacionada con la identidad:
 * el registro (cifrado de contraseña) y la autenticación (verificación de
 * credenciales + emisión del token JWT). El Controller NO debe hacer este
 * trabajo: solo recibe la petición y delega en este servicio.
 */
@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Codificador BCrypt definido en SecurityConfig

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Registra un usuario nuevo cifrando su contraseña antes de persistirla.
     */
    public Usuario registrarUsuario(Usuario usuario) {
        log.info("Registrando nuevo usuario: {}", usuario.getUsername());
        // 1. Ciframos la contraseña antes de guardarla (nunca se guarda en texto plano)
        String passwordCifrada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordCifrada);

        // 2. Guardamos el usuario con la contraseña cifrada
        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario registrado con id {}", guardado.getId());
        return guardado;
    }

    /**
     * Autentica a un usuario: busca por username, compara la contraseña con
     * BCrypt y, si es correcta, genera y devuelve el token JWT.
     *
     * Si las credenciales son inválidas lanza una excepción de dominio que el
     * GlobalExceptionHandler traduce a un 401 UNAUTHORIZED.
     */
    public String autenticar(String username, String password) {
        log.info("Intento de autenticación para el usuario: {}", username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Autenticación fallida: el usuario {} no existe", username);
                    return new CredencialesInvalidasException("Credenciales incorrectas");
                });

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            log.warn("Autenticación fallida: contraseña incorrecta para {}", username);
            throw new CredencialesInvalidasException("Credenciales incorrectas");
        }

        log.info("Autenticación exitosa para {}. Generando token JWT", username);
        return jwtTokenProvider.createToken(usuario.getUsername());
    }
}
