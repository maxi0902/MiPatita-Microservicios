package com.exampleMiPatita.demo.model;

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

@Entity
@Table(name = "actividades")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Actividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idActividad;

    @NotNull(message = "El ID de la mascota es obligatorio")
    @Column(nullable = false)
    private Integer idMascota;

    @NotBlank(message = "El tipo de actividad no puede estar vacio")
    private String tipoActividad;

    @NotNull(message = "La duracion es obligatoria")
    @Min(value = 1, message = "La duracion debe ser de al menos 1 minuto")
    private Integer duracionMinutos;

    private Integer pasos; 

    @NotBlank(message = "La fecha es obligatoria")
    private String fecha;

    private String observacion; 
}