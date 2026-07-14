package com.mipatita.notificaciones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mipatita.notificaciones.model.Notificacion;
import com.mipatita.notificaciones.service.NotificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/notificaciones")
@Tag(name = "Notificaciones", description = "Genera y consulta notificaciones agregando datos de otros microservicios")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping
    @Operation(summary = "Listar todas las notificaciones",
            description = "Obtiene todas las notificaciones generadas y almacenadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay notificaciones")
    })
    public ResponseEntity<List<Notificacion>> listarNotificaciones() {
        List<Notificacion> notificaciones = notificacionService.getNotificaciones();
        if (notificaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notificaciones);
    }

    @GetMapping("/mascota/{idMascota}")
    @Operation(summary = "Listar notificaciones almacenadas de una mascota",
            description = "Obtiene las notificaciones ya generadas para una mascota especifica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "La mascota no tiene notificaciones")
    })
    public ResponseEntity<List<Notificacion>> listarPorMascota(@PathVariable Integer idMascota) {
        List<Notificacion> notificaciones = notificacionService.getNotificacionesPorMascota(idMascota);
        if (notificaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notificaciones);
    }

    @PostMapping("/generar/{idMascota}")
    @Operation(summary = "Generar notificaciones de una mascota",
            description = "Consulta ms-recordatorios y ms-vacunas via Feign, genera las notificaciones y las guarda.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notificaciones generadas correctamente"),
            @ApiResponse(responseCode = "204", description = "La mascota no tiene recordatorios ni vacunas")
    })
    public ResponseEntity<List<Notificacion>> generar(@PathVariable Integer idMascota) {
        List<Notificacion> generadas = notificacionService.generarParaMascota(idMascota);
        if (generadas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(generadas);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una notificacion",
            description = "Elimina la notificacion identificada por el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notificacion eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe una notificacion con ese id")
    })
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        boolean eliminado = notificacionService.deleteNotificacion(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notificacion no encontrada");
        }
        return ResponseEntity.noContent().build();
    }
}
