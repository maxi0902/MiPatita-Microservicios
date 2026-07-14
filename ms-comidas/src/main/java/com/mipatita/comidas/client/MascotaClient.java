package com.mipatita.comidas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente Feign hacia el microservicio de Mascotas.
 * El nombre "ms-mascotas" se resuelve por Eureka.
 */
@FeignClient(name = "ms-mascotas")
public interface MascotaClient {

    @GetMapping("/api/v1/mascotas/verificar/{id}")
    Boolean verificarMascota(@PathVariable("id") Integer id);
}
