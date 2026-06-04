package com.exampleMiPatita.demo.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exampleMiPatita.demo.model.Usuario;
import com.exampleMiPatita.demo.security.LoginRequest;
import com.exampleMiPatita.demo.security.RegistroRequest;
import com.exampleMiPatita.demo.service.UsuarioService;

import jakarta.validation.Valid;

/**
 * Controlador de autenticación (capa CONTROLLER).
 *
 * Su única responsabilidad es recibir la petición HTTP, validar el formato del
 * body (@Valid) y delegar TODA la lógica de negocio en UsuarioService.
 * No accede al repositorio ni cifra contraseñas ni genera tokens directamente.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        // La verificación de credenciales y la creación del token ocurren en el Service.
        // Si las credenciales son inválidas, el GlobalExceptionHandler responde 401.
        String token = usuarioService.autenticar(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroRequest registroRequest) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(registroRequest.getUsername());
        nuevoUsuario.setPassword(registroRequest.getPassword());
        nuevoUsuario.setRole(registroRequest.getRole());

        usuarioService.registrarUsuario(nuevoUsuario);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
}
