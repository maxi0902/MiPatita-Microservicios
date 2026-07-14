package com.mipatita.usuarios.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
@Schema(description = "Usuario del sistema con credenciales para autenticacion")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico del usuario", example = "1")
    private Integer id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Nombre de usuario unico", example = "maxito")
    private String username;

    @Column(nullable = false)
    @Schema(description = "Contrasena cifrada con BCrypt (nunca se devuelve en claro)")
    private String password;

    @Schema(description = "Rol del usuario", example = "ADMIN")
    private String role;
}
