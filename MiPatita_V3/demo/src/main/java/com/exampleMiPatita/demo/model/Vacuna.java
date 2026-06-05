package com.exampleMiPatita.demo.model;

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
public class Vacuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVacuna;

    @NotNull(message = "El ID de la mascota es obligatorio")
    @Column(nullable = false)
    private Integer idMascota;

    @NotBlank(message = "El nombre de la vacuna es obligatorio")
    private String nombreVacuna;

    @NotBlank(message = "La fecha de aplicación es obligatoria")
    private String fechaAplicacion;

    private String veterinario;
}
