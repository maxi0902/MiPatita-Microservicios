package com.exampleMiPatita.demo.model;

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
public class ControlPeso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idControl;

    @NotNull(message = "El ID de la mascota es obligatorio")
    @Column(nullable = false)
    private Integer idMascota;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.1", message = "El peso debe ser mayor a 0")
    private Double pesoKg;

    @NotBlank(message = "La fecha es obligatoria")
    private String fecha;

    private String observacion;
}
