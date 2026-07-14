package com.mipatita.notificaciones.dto;

import lombok.Data;

/**
 * Vista parcial de la Vacuna remota (ms-vacunas) que este servicio necesita.
 */
@Data
public class VacunaDTO {
    private Integer idVacuna;
    private Integer idMascota;
    private String nombreVacuna;
    private String fechaAplicacion;
}
