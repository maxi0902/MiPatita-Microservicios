package com.mipatita.controlpeso.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-mascotas")
public interface MascotaClient {

    @GetMapping("/api/v1/mascotas/verificar/{id}")
    Boolean verificarMascota(@PathVariable("id") Integer id);
}
