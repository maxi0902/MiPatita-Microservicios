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

import com.exampleMiPatita.demo.model.ControlPeso;
import com.exampleMiPatita.demo.service.ControlPesoService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/controles-peso")
public class ControlPesoController {

    @Autowired
    private ControlPesoService controlPesoService;

    @GetMapping
    public ResponseEntity<List<ControlPeso>> listarControles() {
        return ResponseEntity.ok(controlPesoService.getControles());
    }

    @PostMapping
    public ResponseEntity<?> guardarControl(@Valid @RequestBody ControlPeso control) {
        try {
            ControlPeso nuevo = controlPesoService.saveControl(control);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se puede registrar el peso: la mascota no existe");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerControl(@PathVariable Integer id) {
        ControlPeso control = controlPesoService.getControl(id);
        if (control == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Control de peso no encontrado");
        }
        return ResponseEntity.ok(control);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarControl(@PathVariable Integer id, @Valid @RequestBody ControlPeso control) {
        ControlPeso actualizado = controlPesoService.updateControl(id, control);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo actualizar el control de peso");
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarControl(@PathVariable Integer id) {
        boolean eliminado = controlPesoService.deleteControl(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Control de peso no encontrado");
        }
        return ResponseEntity.noContent().build();
    }
}
