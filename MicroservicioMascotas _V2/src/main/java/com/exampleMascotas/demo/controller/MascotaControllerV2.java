package com.exampleMascotas.demo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exampleMascotas.demo.assemblers.MascotaModelAssembler;
import com.exampleMascotas.demo.model.Mascota;
import com.exampleMascotas.demo.service.MascotaService;

/**
 * Versión 2 del controlador de Mascotas CON HATEOAS.
 *
 * A diferencia de MascotaController (v1) que devuelve los datos "planos",
 * esta versión devuelve los recursos enriquecidos con enlaces (_links) que
 * permiten al cliente navegar la API de forma dinámica.
 *
 * Convive con la v1: los endpoints v1 (/api/v1/mascotas) siguen funcionando
 * igual; estos nuevos endpoints viven en /api/v2/mascotas.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v2/mascotas")
public class MascotaControllerV2 {

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private MascotaModelAssembler assembler;

    // Listar todas las mascotas como una colección HATEOAS.
    @GetMapping
    public CollectionModel<EntityModel<Mascota>> listarTodas() {
        List<EntityModel<Mascota>> mascotas = mascotaService.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(mascotas,
                linkTo(methodOn(MascotaControllerV2.class).listarTodas()).withSelfRel());
    }

    // Obtener una mascota por id, con sus enlaces de navegación.
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Mascota>> obtenerMascota(@PathVariable Integer id) {
        Mascota mascota = mascotaService.findById(id);
        if (mascota == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(mascota));
    }
}
