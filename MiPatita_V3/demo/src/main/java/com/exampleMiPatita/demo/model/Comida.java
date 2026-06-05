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
@Table(name = "comidas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idComida;

    @NotNull(message = "El ID de la mascota es obligatorio")
    @Column(nullable = false)
    private Integer idMascota;

    @NotBlank(message = "El tipo de comida es obligatorio")
    private String tipoComida;

    @NotNull(message = "La cantidad en gramos es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser de al menos 1 gramo")
    private Integer cantidadGramos;

    @NotBlank(message = "La fecha es obligatoria")
    private String fecha;

    private String observacion;
}
