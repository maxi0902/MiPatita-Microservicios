package com.exampleMiPatita.demo.controller;

import java.util.List;

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

import com.exampleMiPatita.demo.model.Vacuna;
import com.exampleMiPatita.demo.service.VacunaService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/vacunas")
public class VacunaController {

    @Autowired
    private VacunaService vacunaService;

    @GetMapping
    public ResponseEntity<List<Vacuna>> listarVacunas() {
        return ResponseEntity.ok(vacunaService.getVacunas());
    }

    @PostMapping
    public ResponseEntity<?> guardarVacuna(@Valid @RequestBody Vacuna vacuna) {
        try {
            Vacuna nueva = vacunaService.saveVacuna(vacuna);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se puede registrar la vacuna: la mascota no existe");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerVacuna(@PathVariable Integer id) {
        Vacuna vacuna = vacunaService.getVacuna(id);
        if (vacuna == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vacuna no encontrada");
        }
        return ResponseEntity.ok(vacuna);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVacuna(@PathVariable Integer id, @Valid @RequestBody Vacuna vacuna) {
        Vacuna actualizada = vacunaService.updateVacuna(id, vacuna);
        if (actualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo actualizar la vacuna");
        }
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVacuna(@PathVariable Integer id) {
        boolean eliminado = vacunaService.deleteVacuna(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vacuna no encontrada");
        }
        return ResponseEntity.noContent().build();
    }
}
