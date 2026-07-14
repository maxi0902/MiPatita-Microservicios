package com.mipatita.controlpeso.controller;

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

import com.mipatita.controlpeso.model.ControlPeso;
import com.mipatita.controlpeso.service.ControlPesoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/controles-peso")
@Tag(name = "Controles de peso", description = "Seguimiento del peso de las mascotas")
public class ControlPesoController {

    @Autowired
    private ControlPesoService controlPesoService;

    @GetMapping
    @Operation(summary = "Listar todos los controles de peso",
            description = "Obtiene el listado completo de controles de peso registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay controles de peso registrados")
    })
    public ResponseEntity<List<ControlPeso>> listarControles() {
        List<ControlPeso> controles = controlPesoService.getControles();
        if (controles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(controles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un control de peso por su id",
            description = "Busca un control de peso por su identificador unico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Control encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un control con ese id")
    })
    public ResponseEntity<?> obtenerControl(@PathVariable Integer id) {
        ControlPeso control = controlPesoService.getControl(id);
        if (control == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Control de peso no encontrado");
        }
        return ResponseEntity.ok(control);
    }

    @GetMapping("/mascota/{idMascota}")
    @Operation(summary = "Listar controles de peso de una mascota",
            description = "Obtiene todos los controles de peso de una mascota especifica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "La mascota no tiene controles de peso")
    })
    public ResponseEntity<List<ControlPeso>> listarPorMascota(@PathVariable Integer idMascota) {
        List<ControlPeso> controles = controlPesoService.getControlesPorMascota(idMascota);
        if (controles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(controles);
    }

    @PostMapping
    @Operation(summary = "Registrar un control de peso",
            description = "Crea un nuevo control de peso asociado a una mascota existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Control registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o la mascota no existe")
    })
    public ResponseEntity<ControlPeso> guardarControl(@Valid @RequestBody ControlPeso control) {
        ControlPeso nuevo = controlPesoService.saveControl(control);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un control de peso",
            description = "Reemplaza los datos del control identificado por el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Control actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe un control con ese id"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    public ResponseEntity<?> actualizarControl(@PathVariable Integer id, @Valid @RequestBody ControlPeso control) {
        ControlPeso actualizado = controlPesoService.updateControl(id, control);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo actualizar el control de peso");
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un control de peso",
            description = "Elimina el control identificado por el id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Control eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe un control con ese id")
    })
    public ResponseEntity<?> eliminarControl(@PathVariable Integer id) {
        boolean eliminado = controlPesoService.deleteControl(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Control de peso no encontrado");
        }
        return ResponseEntity.noContent().build();
    }
}
