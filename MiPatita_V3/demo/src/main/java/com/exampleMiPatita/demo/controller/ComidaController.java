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

import com.exampleMiPatita.demo.model.Comida;
import com.exampleMiPatita.demo.service.ComidaService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/comidas")
public class ComidaController {

    @Autowired
    private ComidaService comidaService;

    @GetMapping
    public ResponseEntity<List<Comida>> listarComidas() {
        return ResponseEntity.ok(comidaService.getComidas());
    }

    @PostMapping
    public ResponseEntity<?> guardarComida(@Valid @RequestBody Comida comida) {
        try {
            Comida nuevaComida = comidaService.saveComida(comida);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaComida);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se puede registrar la comida: la mascota no existe");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerComida(@PathVariable Integer id) {
        Comida comida = comidaService.getComida(id);
        if (comida == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comida no encontrada");
        }
        return ResponseEntity.ok(comida);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarComida(@PathVariable Integer id, @Valid @RequestBody Comida comida) {
        Comida actualizada = comidaService.updateComida(id, comida);
        if (actualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo actualizar la comida");
        }
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarComida(@PathVariable Integer id) {
        boolean eliminado = comidaService.deleteComida(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comida no encontrada");
        }
        return ResponseEntity.noContent().build();
    }
}
