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

import com.exampleMiPatita.demo.model.Actividad;
import com.exampleMiPatita.demo.service.ActividadService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/actividades")
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @GetMapping
    public ResponseEntity<List<Actividad>> listarActividades() {
        return ResponseEntity.ok(actividadService.getActividades());
    }

    @PostMapping
    public ResponseEntity<?> guardarActividad(@RequestBody @Valid Actividad actividad) {
        try {
            Actividad nuevaActividad = actividadService.saveActividad(actividad);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaActividad);
        } catch (RuntimeException e) {
        
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se puede crear la actividad: la mascota no existe");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerActividad(@PathVariable Integer id) {
        Actividad actividad = actividadService.getActividad(id);
        if (actividad == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Actividad no encontrada");
        }
        return ResponseEntity.ok(actividad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarActividad(@PathVariable Integer id,@Valid @RequestBody Actividad actividad) {
        Actividad actualizada = actividadService.updateActividad(id, actividad);
        if (actualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se pudo actualizar la actividad");
        }
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarActividad(@PathVariable Integer id) {
        boolean eliminado = actividadService.deleteActividad(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Actividad no encontrada");
        }
        return ResponseEntity.noContent().build();
    }
}