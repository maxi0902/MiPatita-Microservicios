package com.mipatita.notificaciones.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notificaciones")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Notificacion generada para una mascota a partir de sus recordatorios y vacunas")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    @Schema(description = "Identificador unico de la notificacion", example = "1")
    private Integer idNotificacion;

    @Column(name = "id_mascota")
    @Schema(description = "Id de la mascota a la que corresponde la notificacion", example = "1")
    private Integer idMascota;

    @Column(name = "tipo")
    @Schema(description = "Origen de la notificacion", example = "RECORDATORIO")
    private String tipo;

    @Column(name = "mensaje")
    @Schema(description = "Texto de la notificacion", example = "Recordatorio pendiente: Vacuna antirrabica")
    private String mensaje;
}
