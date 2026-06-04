package com.exampleMascotas.demo.controller;

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

import com.exampleMascotas.demo.model.Mascota;
import com.exampleMascotas.demo.service.MascotaService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    // Listar: Uso de ResponseEntity para una respuesta limpia
    @GetMapping
    public ResponseEntity<List<Mascota>> listarTodas() {
        return ResponseEntity.ok(mascotaService.findAll());
    }

    // Verificar: Uso de Boolean y estado 404 si no existe
    @GetMapping("/verificar/{id}")
    public ResponseEntity<Boolean> verificarMascota(@PathVariable Integer id) {
        boolean existe = mascotaService.existsById(id);
        if (!existe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
        return ResponseEntity.ok(true);
    }

    // Obtener una mascota por id: 200 con el detalle, o 404 si no existe.
    // Este endpoint es consumido por MiPatita vía Feign (obtenerMascota).
    @GetMapping("/{id}")
    public ResponseEntity<Mascota> obtenerMascota(@PathVariable Integer id) {
        Mascota mascota = mascotaService.findById(id);
        if (mascota == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mascota);
    }

    // Guardar: Uso de @Valid, @RequestBody y estado 201 CREATED
    @PostMapping
    public ResponseEntity<Mascota> guardarMascota(@Valid @RequestBody Mascota mascota) {
        Mascota nuevaMascota = mascotaService.save(mascota);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMascota);
    }

    // Actualizar: Uso de @Valid y manejo de null a través del Service
    @PutMapping("/{id}")
    public ResponseEntity<Mascota> actualizarMascota(@PathVariable Integer id, @Valid @RequestBody Mascota mascotaDetalles) {
        Mascota actualizada = mascotaService.update(id, mascotaDetalles);
        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMascota(@PathVariable Integer id) {
        boolean eliminado = mascotaService.deleteById(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}