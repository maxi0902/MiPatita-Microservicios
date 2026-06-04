package com.exampleMiPatita.demo.webclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.exampleMiPatita.demo.model.Mascota;

@FeignClient(name = "microservicio-mascotas", url = "${mascotas.service.url:http://localhost:8081/api/v1/mascotas}")
public interface MascotaClient {
    
    @GetMapping("/verificar/{id}")
    Boolean verificarMascota(@PathVariable("id") Integer id);

    @GetMapping
    List<Mascota> listarMascotas();
    
    @GetMapping("/{id}")
    Mascota obtenerMascota(@PathVariable("id") Integer id);

    @PostMapping
    Mascota guardarMascota(@RequestBody Mascota mascota);

    @PutMapping("/{id}")
    Mascota actualizarMascota(@PathVariable("id") Integer id, @RequestBody Mascota mascota);

    @DeleteMapping("/{id}")
    void eliminarMascota(@PathVariable("id") Integer id);
}