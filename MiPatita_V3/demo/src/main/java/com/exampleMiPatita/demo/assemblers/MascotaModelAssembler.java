package com.exampleMiPatita.demo.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.exampleMiPatita.demo.controller.MascotaControllerV2;
import com.exampleMiPatita.demo.model.Mascota;

/**
 * Ensamblador de recursos HATEOAS para Mascota.
 * Convierte una Mascota en un EntityModel<Mascota> (la mascota + sus _links).
 */
@Component
public class MascotaModelAssembler implements RepresentationModelAssembler<Mascota, EntityModel<Mascota>> {

    @Override
    public EntityModel<Mascota> toModel(Mascota mascota) {
        return EntityModel.of(mascota,
                linkTo(methodOn(MascotaControllerV2.class).obtenerMascota(mascota.getIdMascota())).withSelfRel(),
                linkTo(methodOn(MascotaControllerV2.class).listarTodas()).withRel("mascotas"));
    }
}
