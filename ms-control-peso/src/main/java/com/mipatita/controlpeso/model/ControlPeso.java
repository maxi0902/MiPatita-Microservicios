package com.mipatita.controlpeso.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "controles_peso")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Medicion de peso de una mascota en una fecha")
public class ControlPeso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_control")
    @Schema(description = "Identificador unico del control de peso", example = "1")
    private Integer idControl;

    @NotNull(message = "El ID de la mascota es obligatorio")
    @Column(name = "id_mascota", nullable = false)
    @Schema(description = "Id de la mascota medida", example = "1")
    private Integer idMascota;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.1", message = "El peso debe ser mayor a 0")
    @Column(name = "peso_kg")
    @Schema(description = "Peso de la mascota en kilogramos", example = "12.5")
    private Double pesoKg;

    @NotBlank(message = "La fecha es obligatoria")
    @Column(name = "fecha")
    @Schema(description = "Fecha de la medicion en formato AAAA-MM-DD", example = "2026-07-12")
    private String fecha;

    @Column(name = "observacion")
    @Schema(description = "Observaciones adicionales (opcional)", example = "Peso estable")
    private String observacion;
}
