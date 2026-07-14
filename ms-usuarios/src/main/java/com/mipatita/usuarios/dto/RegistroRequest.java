package com.mipatita.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos para registrar un nuevo usuario")
public class RegistroRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Schema(description = "Nombre de usuario unico", example = "maxito")
    private String username;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 4, message = "La contrasena debe tener al menos 4 caracteres")
    @Schema(description = "Contrasena (minimo 4 caracteres)", example = "1234")
    private String password;

    @Schema(description = "Rol del usuario", example = "ADMIN")
    private String role;
}
