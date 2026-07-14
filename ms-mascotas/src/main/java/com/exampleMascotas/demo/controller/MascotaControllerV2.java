package com.exampleMascotas.demo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exampleMascotas.demo.assemblers.MascotaModelAssembler;
import com.exampleMascotas.demo.model.Mascota;
import com.exampleMascotas.demo.service.MascotaService;

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
        List<EntityModel<Mascota>> mascotas = mascotaService.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(mascotas,
                linkTo(methodOn(MascotaControllerV2.class).listarTodas()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Mascota>> obtenerMascota(@PathVariable Integer id) {
        Mascota mascota = mascotaService.findById(id);
        if (mascota == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(mascota));
    }

    @GetMapping(value = "/raza/{raza}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Mascota>> getMascotasByRaza(@PathVariable String raza) {
        List<EntityModel<Mascota>> mascotas = mascotaService.findByRaza(raza).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(mascotas,
                linkTo(methodOn(MascotaControllerV2.class).getMascotasByRaza(raza)).withSelfRel());
    }

    @GetMapping(value = "/edad/{edad}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Mascota>> getMascotasByEdad(@PathVariable Integer edad) {
        List<EntityModel<Mascota>> mascotas = mascotaService.findByEdad(edad).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(mascotas,
                linkTo(methodOn(MascotaControllerV2.class).getMascotasByEdad(edad)).withSelfRel());
    }

    @GetMapping(value = "/nivel-energia/{nivelEnergia}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Mascota>> getMascotasByNivelEnergia(@PathVariable String nivelEnergia) {
        List<EntityModel<Mascota>> mascotas = mascotaService.findByNivelEnergia(nivelEnergia).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(mascotas,
                linkTo(methodOn(MascotaControllerV2.class).getMascotasByNivelEnergia(nivelEnergia)).withSelfRel());
    }

    @GetMapping(value = "/edad/{edadMin}/{edadMax}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Mascota>> getMascotasByEdadBetween(
            @PathVariable Integer edadMin, @PathVariable Integer edadMax) {
        List<EntityModel<Mascota>> mascotas = mascotaService.findByEdadBetween(edadMin, edadMax).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(mascotas,
                linkTo(methodOn(MascotaControllerV2.class).getMascotasByEdadBetween(edadMin, edadMax)).withSelfRel());
    }

    @GetMapping(value = "/raza/{raza}/total", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<TotalMascotasPorRaza> getTotalMascotasByRaza(@PathVariable String raza) {
        long total = mascotaService.countByRaza(raza);

        return EntityModel.of(new TotalMascotasPorRaza(raza, total),
                linkTo(methodOn(MascotaControllerV2.class).getTotalMascotasByRaza(raza)).withSelfRel(),
                linkTo(methodOn(MascotaControllerV2.class).getMascotasByRaza(raza)).withRel("mascotas-de-la-raza"));
    }

    public record TotalMascotasPorRaza(String raza, long totalMascotas) {
    }
}
