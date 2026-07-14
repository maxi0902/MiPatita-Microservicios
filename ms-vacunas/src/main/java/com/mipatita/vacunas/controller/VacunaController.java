package com.mipatita.vacunas.controller;

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

import com.mipatita.vacunas.model.Vacuna;
import com.mipatita.vacunas.service.VacunaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/vacunas")
@Tag(name = "Vacunas", description = "Gestion de vacunas de las mascotas")
public class VacunaController {

    @Autowired
    private VacunaService vacunaService;

    @GetMapping
    @Operation(summary = "Listar todas las vacunas",
            description = "Obtiene el listado completo de vacunas registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay vacunas registradas")
    })
    public ResponseEntity<List<Vacuna>> listarVacunas() {
        List<Vacuna> vacunas = vacunaService.getVacunas();
        if (vacunas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vacunas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una vacuna por su id",
            description = "Busca una vacuna por su identificador unico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacuna encontrada"),
            @ApiResponse(responseCode = "404", description = "No existe una vacuna con ese id")
    })
    public ResponseEntity<?> obtenerVacuna(@PathVariable Integer id) {
        Vacuna vacuna = vacunaService.getVacuna(id);
        if (vacuna == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vacuna no encontrada");
        }
        return ResponseEntity.ok(vacuna);
    }

    @GetMapping("/mascota/{idMascota}")
    @Operation(summary = "Listar vacunas de una mascota",
            description = "Obtiene todas las vacunas de una mascota especifica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "La mascota no tiene vacunas")
    })
    public ResponseEntity<List<Vacuna>> listarPorMascota(@PathVariable Integer idMascota) {
        List<Vacuna> vacunas = vacunaService.getVacunasPorMascota(idMascota);
        if (vacunas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vacunas);
    }

    @PostMapping
    @Operation(summary = "Registrar una vacuna",
            description = "Crea una nueva vacuna asociada a una mascota existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vacuna registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o la mascota no existe")
    })
    public ResponseEntity<Vacuna> guardarVacuna(@Valid @RequestBody Vacuna vacuna) {
        Vacuna nueva = vacunaService.saveVacuna(vacuna);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una vacuna",
            description = "Reemplaza los datos de la vacuna identificada por el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacuna actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe una vacuna con ese id"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    public ResponseEntity<?> actualizarVacuna(@PathVariable Integer id, @Valid @RequestBody Vacuna vacuna) {
        Vacuna actualizada = vacunaService.updateVacuna(id, vacuna);
        if (actualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo actualizar la vacuna");
        }
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una vacuna",
            description = "Elimina la vacuna identificada por el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vacuna eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe una vacuna con ese id")
    })
    public ResponseEntity<?> eliminarVacuna(@PathVariable Integer id) {
        boolean eliminado = vacunaService.deleteVacuna(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vacuna no encontrada");
        }
        return ResponseEntity.noContent().build();
    }
}
