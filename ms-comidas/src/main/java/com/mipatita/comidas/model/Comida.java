package com.mipatita.comidas.model;

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

@Entity
@Table(name = "comidas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Registro de una comida entregada a una mascota")
public class Comida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comida")
    @Schema(description = "Identificador unico de la comida", example = "1")
    private Integer idComida;

    @NotNull(message = "El ID de la mascota es obligatorio")
    @Column(name = "id_mascota", nullable = false)
    @Schema(description = "Id de la mascota a la que se le entrego la comida", example = "1")
    private Integer idMascota;

    @NotBlank(message = "El tipo de comida es obligatorio")
    @Column(name = "tipo_comida")
    @Schema(description = "Tipo de comida entregada", example = "Croquetas")
    private String tipoComida;

    @NotNull(message = "La cantidad en gramos es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser de al menos 1 gramo")
    @Column(name = "cantidad_gramos")
    @Schema(description = "Cantidad de comida en gramos", example = "150")
    private Integer cantidadGramos;

    @NotBlank(message = "La fecha es obligatoria")
    @Column(name = "fecha")
    @Schema(description = "Fecha de la comida en formato AAAA-MM-DD", example = "2026-07-12")
    private String fecha;

    @Column(name = "observacion")
    @Schema(description = "Observaciones adicionales (opcional)", example = "Comio todo")
    private String observacion;
}
