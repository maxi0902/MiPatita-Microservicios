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
@Table(name = "recordatorios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recordatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRecordatorio;

    @NotNull(message = "El ID de la mascota es obligatorio")
    @Column(nullable = false)
    private Integer idMascota;

    @NotBlank(message = "El tipo de recordatorio es obligatorio")
    private String tipoRecordatorio;

    private Integer frecuenciaDias;

    @NotBlank(message = "La fecha próxima es obligatoria")
    private String fechaProxima;

    private Boolean completado;
}
