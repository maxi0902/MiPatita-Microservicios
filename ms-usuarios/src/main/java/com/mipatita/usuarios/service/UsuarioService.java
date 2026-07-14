package com.mipatita.usuarios.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mipatita.usuarios.exception.CredencialesInvalidasException;
import com.mipatita.usuarios.model.Usuario;
import com.mipatita.usuarios.repository.UsuarioRepository;
import com.mipatita.usuarios.security.JwtTokenProvider;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public Usuario registrarUsuario(Usuario usuario) {
        log.info("Registrando nuevo usuario: {}", usuario.getUsername());
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) { //regla de negocio
            throw new IllegalArgumentException("El nombre de usuario ya esta en uso");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario registrado con id {}", guardado.getId());
        return guardado;
    }

    public String autenticar(String username, String password) {
        log.info("Intento de autenticacion para el usuario: {}", username);
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Autenticacion fallida: el usuario {} no existe", username);
                    return new CredencialesInvalidasException("Credenciales incorrectas");
                });

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            log.warn("Autenticacion fallida: contrasena incorrecta para {}", username);
            throw new CredencialesInvalidasException("Credenciales incorrectas");
        }

        log.info("Autenticacion exitosa para {}. Generando token JWT", username);
        return jwtTokenProvider.createToken(usuario.getUsername());
    }

    public boolean validarToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }
}
