package com.mipatita.comidas.controller;

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

import com.mipatita.comidas.model.Comida;
import com.mipatita.comidas.service.ComidaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/comidas")
@Tag(name = "Comidas", description = "Gestion de la alimentacion de las mascotas")
public class ComidaController {

    @Autowired
    private ComidaService comidaService;

    @GetMapping
    @Operation(summary = "Listar todas las comidas",
            description = "Obtiene el listado completo de comidas registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay comidas registradas")
    })
    public ResponseEntity<List<Comida>> listarComidas() {
        List<Comida> comidas = comidaService.getComidas();
        if (comidas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comidas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una comida por su id",
            description = "Busca una comida por su identificador unico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comida encontrada"),
            @ApiResponse(responseCode = "404", description = "No existe una comida con ese id")
    })
    public ResponseEntity<?> obtenerComida(@PathVariable Integer id) {
        Comida comida = comidaService.getComida(id);
        if (comida == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comida no encontrada");
        }
        return ResponseEntity.ok(comida);
    }

    @GetMapping("/mascota/{idMascota}")
    @Operation(summary = "Listar comidas de una mascota",
            description = "Obtiene todas las comidas registradas para una mascota especifica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "La mascota no tiene comidas registradas")
    })
    public ResponseEntity<List<Comida>> listarPorMascota(@PathVariable Integer idMascota) {
        List<Comida> comidas = comidaService.getComidasPorMascota(idMascota);
        if (comidas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comidas);
    }

    @PostMapping
    @Operation(summary = "Registrar una comida",
            description = "Crea una nueva comida asociada a una mascota existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comida registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o la mascota no existe")
    })
    public ResponseEntity<Comida> guardarComida(@Valid @RequestBody Comida comida) {
        Comida nueva = comidaService.saveComida(comida);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una comida",
            description = "Reemplaza los datos de la comida identificada por el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comida actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe una comida con ese id"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    public ResponseEntity<?> actualizarComida(@PathVariable Integer id, @Valid @RequestBody Comida comida) {
        Comida actualizada = comidaService.updateComida(id, comida);
        if (actualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo actualizar la comida");
        }
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una comida",
            description = "Elimina la comida identificada por el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comida eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe una comida con ese id")
    })
    public ResponseEntity<?> eliminarComida(@PathVariable Integer id) {
        boolean eliminado = comidaService.deleteComida(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comida no encontrada");
        }
        return ResponseEntity.noContent().build();
    }
}
