package com.mipatita.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Credenciales para iniciar sesion")
public class LoginRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Schema(description = "Nombre de usuario", example = "maxito")
    private String username;

    @NotBlank(message = "La contrasena es obligatoria")
    @Schema(description = "Contrasena del usuario", example = "1234")
    private String password;
}
