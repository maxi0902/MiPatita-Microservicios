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

import com.exampleMiPatita.demo.model.Recordatorio;
import com.exampleMiPatita.demo.service.RecordatorioService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/recordatorios")
public class RecordatorioController {

    @Autowired
    private RecordatorioService recordatorioService;

    @GetMapping
    public ResponseEntity<List<Recordatorio>> listarRecordatorios() {
        return ResponseEntity.ok(recordatorioService.getRecordatorios());
    }

    @PostMapping
    public ResponseEntity<?> guardarRecordatorio(@Valid @RequestBody Recordatorio recordatorio) {
        Recordatorio nuevoRecordatorio = recordatorioService.saveRecordatorio(recordatorio);
        if (nuevoRecordatorio == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se puede crear el recordatorio: la mascota no existe");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRecordatorio);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerRecordatorio(@PathVariable Integer id) {
        Recordatorio recordatorio = recordatorioService.getRecordatorio(id);
        if (recordatorio == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recordatorio no encontrado");
        }
        return ResponseEntity.ok(recordatorio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarRecordatorio(@PathVariable Integer id, @Valid @RequestBody Recordatorio recordatorio) {
        Recordatorio actualizado = recordatorioService.updateRecordatorio(id, recordatorio);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se pudo actualizar el recordatorio");
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarRecordatorio(@PathVariable Integer id) {
        boolean eliminado = recordatorioService.deleteRecordatorio(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recordatorio no encontrado");
        }
        return ResponseEntity.noContent().build();
    }
}