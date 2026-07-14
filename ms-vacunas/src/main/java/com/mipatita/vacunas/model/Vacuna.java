package com.mipatita.vacunas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vacunas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Vacuna aplicada a una mascota")
public class Vacuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vacuna")
    @Schema(description = "Identificador unico de la vacuna", example = "1")
    private Integer idVacuna;

    @NotNull(message = "El ID de la mascota es obligatorio")
    @Column(name = "id_mascota", nullable = false)
    @Schema(description = "Id de la mascota vacunada", example = "1")
    private Integer idMascota;

    @NotBlank(message = "El nombre de la vacuna es obligatorio")
    @Column(name = "nombre_vacuna")
    @Schema(description = "Nombre de la vacuna aplicada", example = "Antirrabica")
    private String nombreVacuna;

    @NotBlank(message = "La fecha de aplicacion es obligatoria")
    @Column(name = "fecha_aplicacion")
    @Schema(description = "Fecha de aplicacion en formato AAAA-MM-DD", example = "2026-05-14")
    private String fechaAplicacion;

    @Column(name = "veterinario")
    @Schema(description = "Nombre del veterinario que aplico la vacuna", example = "Dra. Carolina Rojas")
    private String veterinario;
}
