package com.mipatita.notificaciones.dto;

import lombok.Data;

/**
 * Vista parcial del Recordatorio remoto (ms-recordatorios) que este servicio necesita.
 */
@Data
public class RecordatorioDTO {
    private Integer idRecordatorio;
    private Integer idMascota;
    private String tipoRecordatorio;
    private String fechaProxima;
    private Boolean completado;
}
