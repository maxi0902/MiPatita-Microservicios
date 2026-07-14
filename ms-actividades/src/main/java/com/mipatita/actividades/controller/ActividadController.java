package com.mipatita.actividades.controller;

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

import com.mipatita.actividades.model.Actividad;
import com.mipatita.actividades.service.ActividadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/actividades")
@Tag(name = "Actividades", description = "Gestion de actividades fisicas de las mascotas")
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @GetMapping
    @Operation(summary = "Listar todas las actividades",
            description = "Obtiene el listado completo de actividades registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay actividades registradas")
    })
    public ResponseEntity<List<Actividad>> listarActividades() {
        List<Actividad> actividades = actividadService.getActividades();
        if (actividades.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(actividades);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una actividad por su id",
            description = "Busca una actividad por su identificador unico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actividad encontrada"),
            @ApiResponse(responseCode = "404", description = "No existe una actividad con ese id")
    })
    public ResponseEntity<?> obtenerActividad(@PathVariable Integer id) {
        Actividad actividad = actividadService.getActividad(id);
        if (actividad == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Actividad no encontrada");
        }
        return ResponseEntity.ok(actividad);
    }

    @GetMapping("/mascota/{idMascota}")
    @Operation(summary = "Listar actividades de una mascota",
            description = "Obtiene todas las actividades registradas para una mascota especifica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "La mascota no tiene actividades")
    })
    public ResponseEntity<List<Actividad>> listarPorMascota(@PathVariable Integer idMascota) {
        List<Actividad> actividades = actividadService.getActividadesPorMascota(idMascota);
        if (actividades.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(actividades);
    }

    @PostMapping
    @Operation(summary = "Registrar una actividad",
            description = "Crea una nueva actividad asociada a una mascota existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Actividad creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o la mascota no existe")
    })
    public ResponseEntity<Actividad> guardarActividad(@Valid @RequestBody Actividad actividad) {
        Actividad nueva = actividadService.saveActividad(actividad);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una actividad",
            description = "Reemplaza los datos de la actividad identificada por el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actividad actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe una actividad con ese id"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    public ResponseEntity<?> actualizarActividad(@PathVariable Integer id, @Valid @RequestBody Actividad actividad) {
        Actividad actualizada = actividadService.updateActividad(id, actividad);
        if (actualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo actualizar la actividad");
        }
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una actividad",
            description = "Elimina la actividad identificada por el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Actividad eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe una actividad con ese id")
    })
    public ResponseEntity<?> eliminarActividad(@PathVariable Integer id) {
        boolean eliminado = actividadService.deleteActividad(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Actividad no encontrada");
        }
        return ResponseEntity.noContent().build();
    }
}
