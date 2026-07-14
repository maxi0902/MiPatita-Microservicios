package com.mipatita.notificaciones.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mipatita.notificaciones.dto.RecordatorioDTO;

/**
 * Cliente Feign hacia ms-recordatorios (resuelto por Eureka).
 */
@FeignClient(name = "ms-recordatorios")
public interface RecordatorioClient {

    @GetMapping("/api/v1/recordatorios/mascota/{idMascota}")
    List<RecordatorioDTO> obtenerPorMascota(@PathVariable("idMascota") Integer idMascota);
}
