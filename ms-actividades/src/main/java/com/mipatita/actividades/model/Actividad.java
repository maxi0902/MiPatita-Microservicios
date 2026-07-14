package com.mipatita.actividades.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //esto convierte la clase en una tabla y representa la tabla, la mapea
@Table(name = "actividades") //aca se le coloca el nombre a la tabla
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Actividad fisica realizada por una mascota")
public class Actividad {

    @Id //clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id autogenerado
    @Column(name = "id_actividad")
    @Schema(description = "Identificador unico de la actividad", example = "1")
    private Integer idActividad;

    @NotNull(message = "El ID de la mascota es obligatorio")
    @Column(name = "id_mascota", nullable = false)
    @Schema(description = "Id de la mascota que realizo la actividad", example = "1")
    private Integer idMascota;

    @NotBlank(message = "El tipo de actividad no puede estar vacio")
    @Column(name = "tipo_actividad")
    @Schema(description = "Tipo de actividad realizada", example = "Paseo")
    private String tipoActividad;

    @NotNull(message = "La duracion es obligatoria")
    @Min(value = 1, message = "La duracion debe ser de al menos 1 minuto")
    @Column(name = "duracion_minutos")
    @Schema(description = "Duracion de la actividad en minutos", example = "45")
    private Integer duracionMinutos;

    @Column(name = "pasos")
    @Schema(description = "Cantidad de pasos registrados (opcional)", example = "3200")
    private Integer pasos;

    @NotBlank(message = "La fecha es obligatoria")
    @Column(name = "fecha")
    @Schema(description = "Fecha de la actividad en formato AAAA-MM-DD", example = "2026-06-20")
    private String fecha;

    @Column(name = "observacion")
    @Schema(description = "Observaciones adicionales (opcional)", example = "Muy activo")
    private String observacion;
}
