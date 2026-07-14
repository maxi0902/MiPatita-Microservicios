package com.mipatita.recordatorios.model;

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
@Table(name = "recordatorios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Recordatorio de un cuidado o evento de una mascota")
public class Recordatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recordatorio")
    @Schema(description = "Identificador unico del recordatorio", example = "1")
    private Integer idRecordatorio;

    @NotNull(message = "El ID de la mascota es obligatorio")
    @Column(name = "id_mascota", nullable = false)
    @Schema(description = "Id de la mascota asociada al recordatorio", example = "1")
    private Integer idMascota;

    @NotBlank(message = "El tipo de recordatorio es obligatorio")
    @Column(name = "tipo_recordatorio")
    @Schema(description = "Tipo de recordatorio", example = "Vacuna antirrabica")
    private String tipoRecordatorio;

    @Column(name = "frecuencia_dias")
    @Schema(description = "Cada cuantos dias se repite (opcional)", example = "30")
    private Integer frecuenciaDias;

    @NotBlank(message = "La fecha proxima es obligatoria")
    @Column(name = "fecha_proxima")
    @Schema(description = "Fecha del proximo recordatorio en formato AAAA-MM-DD", example = "2026-08-01")
    private String fechaProxima;

    @Column(name = "completado")
    @Schema(description = "Indica si el recordatorio ya se cumplio", example = "false")
    private Boolean completado;
}
