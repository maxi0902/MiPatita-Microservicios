package com.mipatita.notificaciones.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mipatita.notificaciones.dto.VacunaDTO;

/**
 * Cliente Feign hacia ms-vacunas (resuelto por Eureka).
 */
@FeignClient(name = "ms-vacunas")
public interface VacunaClient {

    @GetMapping("/api/v1/vacunas/mascota/{idMascota}")
    List<VacunaDTO> obtenerPorMascota(@PathVariable("idMascota") Integer idMascota);
}
