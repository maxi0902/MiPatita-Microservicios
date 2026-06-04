package com.exampleMiPatita.demo.controller;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exampleMiPatita.demo.model.Mascota;
import com.exampleMiPatita.demo.service.MascotaService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @GetMapping
    public ResponseEntity<List<Mascota>> listarMascotas() {
        return ResponseEntity.ok(mascotaService.getMascotas());
    }

    @PostMapping
    public ResponseEntity<Mascota> guardarMascota(@Valid @RequestBody Mascota mascota) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mascotaService.saveMascota(mascota));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerMascota(@PathVariable Integer id) {
        Mascota mascota = mascotaService.getMascota(id);
        if (mascota == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mascota no encontrada");
        }
        return ResponseEntity.ok(mascota);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMascota(@PathVariable Integer id, @Valid @RequestBody Mascota mascota) {
        Mascota actualizada = mascotaService.updateMascota(id, mascota);
        if (actualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mascota no encontrada");
        }
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMascota(@PathVariable Integer id) {
        boolean eliminado = mascotaService.deleteMascota(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mascota no encontrada");
        }
        return ResponseEntity.noContent().build();
    }
}