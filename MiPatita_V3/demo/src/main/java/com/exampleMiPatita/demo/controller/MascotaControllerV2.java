package com.exampleMiPatita.demo.controller;

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

import com.exampleMiPatita.demo.assemblers.MascotaModelAssembler;
import com.exampleMiPatita.demo.model.Mascota;
import com.exampleMiPatita.demo.service.MascotaService;

/**
 * Versión 2 del controlador de Mascotas CON HATEOAS.
 * Convive con la v1 (/api/v1/mascotas); esta vive en /api/v2/mascotas y
 * devuelve los recursos enriquecidos con enlaces (_links).
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v2/mascotas")
public class MascotaControllerV2 {

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private MascotaModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<Mascota>> listarTodas() {
        List<EntityModel<Mascota>> mascotas = mascotaService.getMascotas().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(mascotas,
                linkTo(methodOn(MascotaControllerV2.class).listarTodas()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Mascota>> obtenerMascota(@PathVariable Integer id) {
        Mascota mascota = mascotaService.getMascota(id);
        if (mascota == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(mascota));
    }
}
