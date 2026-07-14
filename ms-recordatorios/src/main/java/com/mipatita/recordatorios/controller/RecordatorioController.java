package com.mipatita.recordatorios.controller;

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

import com.mipatita.recordatorios.model.Recordatorio;
import com.mipatita.recordatorios.service.RecordatorioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/recordatorios")
@Tag(name = "Recordatorios", description = "Gestion de recordatorios de cuidados de las mascotas")
public class RecordatorioController {

    @Autowired
    private RecordatorioService recordatorioService;

    @GetMapping
    @Operation(summary = "Listar todos los recordatorios",
            description = "Obtiene el listado completo de recordatorios registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay recordatorios registrados")
    })
    public ResponseEntity<List<Recordatorio>> listarRecordatorios() {
        List<Recordatorio> recordatorios = recordatorioService.getRecordatorios();
        if (recordatorios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(recordatorios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un recordatorio por su id",
            description = "Busca un recordatorio por su identificador unico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recordatorio encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un recordatorio con ese id")
    })
    public ResponseEntity<?> obtenerRecordatorio(@PathVariable Integer id) {
        Recordatorio recordatorio = recordatorioService.getRecordatorio(id);
        if (recordatorio == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recordatorio no encontrado");
        }
        return ResponseEntity.ok(recordatorio);
    }

    @GetMapping("/mascota/{idMascota}")
    @Operation(summary = "Listar recordatorios de una mascota",
            description = "Obtiene todos los recordatorios de una mascota especifica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "La mascota no tiene recordatorios")
    })
    public ResponseEntity<List<Recordatorio>> listarPorMascota(@PathVariable Integer idMascota) {
        List<Recordatorio> recordatorios = recordatorioService.getRecordatoriosPorMascota(idMascota);
        if (recordatorios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(recordatorios);
    }

    @GetMapping("/pendientes")
    @Operation(summary = "Listar recordatorios pendientes",
            description = "Obtiene los recordatorios que aun no se han completado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay recordatorios pendientes")
    })
    public ResponseEntity<List<Recordatorio>> listarPendientes() {
        List<Recordatorio> pendientes = recordatorioService.getPendientes();
        if (pendientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pendientes);
    }

    @PostMapping
    @Operation(summary = "Crear un recordatorio",
            description = "Crea un nuevo recordatorio asociado a una mascota existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recordatorio creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o la mascota no existe")
    })
    public ResponseEntity<Recordatorio> guardarRecordatorio(@Valid @RequestBody Recordatorio recordatorio) {
        Recordatorio nuevo = recordatorioService.saveRecordatorio(recordatorio);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un recordatorio",
            description = "Reemplaza los datos del recordatorio identificado por el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recordatorio actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe un recordatorio con ese id"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    public ResponseEntity<?> actualizarRecordatorio(@PathVariable Integer id, @Valid @RequestBody Recordatorio recordatorio) {
        Recordatorio actualizado = recordatorioService.updateRecordatorio(id, recordatorio);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo actualizar el recordatorio");
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un recordatorio",
            description = "Elimina el recordatorio identificado por el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recordatorio eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe un recordatorio con ese id")
    })
    public ResponseEntity<?> eliminarRecordatorio(@PathVariable Integer id) {
        boolean eliminado = recordatorioService.deleteRecordatorio(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recordatorio no encontrado");
        }
        return ResponseEntity.noContent().build();
    }
}
