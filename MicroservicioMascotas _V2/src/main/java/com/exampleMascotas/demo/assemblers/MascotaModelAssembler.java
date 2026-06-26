package com.exampleMascotas.demo.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.exampleMascotas.demo.controller.MascotaControllerV2;
import com.exampleMascotas.demo.model.Mascota;

/**
 * Ensamblador de recursos HATEOAS para la entidad Mascota.
 *
 * Su responsabilidad es convertir un objeto Mascota en un EntityModel<Mascota>,
 * es decir, la mascota + sus enlaces (links) de navegación. Centralizar esto
 * aquí evita repetir la construcción de links en cada método del controlador.
 */
@Component
public class MascotaModelAssembler implements RepresentationModelAssembler<Mascota, EntityModel<Mascota>> {

    @Override
    public EntityModel<Mascota> toModel(Mascota mascota) {
        return EntityModel.of(mascota,
                // self: enlace al detalle de esta misma mascota -> /api/v2/mascotas/{id}
                linkTo(methodOn(MascotaControllerV2.class).obtenerMascota(mascota.getIdMascota())).withSelfRel(),
                // mascotas: enlace a la colección completa -> /api/v2/mascotas
                linkTo(methodOn(MascotaControllerV2.class).listarTodas()).withRel("mascotas"));
    }
}
