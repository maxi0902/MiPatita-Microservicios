package com.mipatita.usuarios.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mipatita.usuarios.dto.LoginRequest;
import com.mipatita.usuarios.dto.RegistroRequest;
import com.mipatita.usuarios.model.Usuario;
import com.mipatita.usuarios.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticacion", description = "Registro, inicio de sesion y validacion de tokens JWT")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    @Operation(summary = "Registrar un usuario",
            description = "Crea un usuario nuevo con su contrasena cifrada (BCrypt).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o el usuario ya existe")
    })
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroRequest registroRequest) {
        Usuario nuevo = new Usuario();
        nuevo.setUsername(registroRequest.getUsername());
        nuevo.setPassword(registroRequest.getPassword());
        nuevo.setRole(registroRequest.getRole());
        usuarioService.registrarUsuario(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion",
            description = "Valida las credenciales y devuelve un token JWT para los endpoints protegidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credenciales validas, devuelve el token"),
            @ApiResponse(responseCode = "401", description = "Usuario o contrasena incorrectos"),
            @ApiResponse(responseCode = "400", description = "Body invalido")
    })
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = usuarioService.autenticar(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @GetMapping("/validar")
    @Operation(summary = "Validar un token JWT",
            description = "Indica si un token es valido. Lo pueden usar otros servicios o el Gateway.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado de la validacion (valido: true/false)")
    })
    public ResponseEntity<Map<String, Boolean>> validar(@RequestParam String token) {
        boolean valido = usuarioService.validarToken(token);
        return ResponseEntity.ok(Collections.singletonMap("valido", valido));
    }

    @GetMapping("/perfil")
    @Operation(summary = "Obtener el usuario autenticado",
            description = "Endpoint protegido: devuelve el nombre del usuario dueno del token JWT enviado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devuelve el username autenticado"),
            @ApiResponse(responseCode = "403", description = "No se envio un token valido")
    })
    public ResponseEntity<Map<String, String>> perfil(Principal principal) {
        return ResponseEntity.ok(Collections.singletonMap("username", principal.getName()));
    }
}
