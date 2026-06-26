package com.pelicula.assembler;

import com.pelicula.controller.PeliculaController;
import com.pelicula.dto.PeliculaResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PeliculaModelAssembler
        implements RepresentationModelAssembler<PeliculaResponseDTO, EntityModel<PeliculaResponseDTO>> {

    @Override
    public EntityModel<PeliculaResponseDTO> toModel(PeliculaResponseDTO pelicula) {
        EntityModel<PeliculaResponseDTO> model = EntityModel.of(pelicula,
                linkTo(methodOn(PeliculaController.class).obtenerPorId(pelicula.getId())).withSelfRel(),
                linkTo(methodOn(PeliculaController.class).obtenerTodas()).withRel("peliculas"),
                linkTo(methodOn(PeliculaController.class).obtenerActivas()).withRel("peliculas-activas"),
                linkTo(methodOn(PeliculaController.class).obtenerPorGenero(pelicula.getGenero())).withRel("peliculas-genero")
        );

        // Cross-service: enlace a las reservas de funciones de esta película
        model.add(Link.of("/api/reservas/funcion/" + pelicula.getId()).withRel("reservas-funcion"));

        // Acciones disponibles según estado de la película
        if (Boolean.TRUE.equals(pelicula.getActivo())) {
            model.add(linkTo(methodOn(PeliculaController.class).eliminar(pelicula.getId())).withRel("retirar"));
            model.add(linkTo(methodOn(PeliculaController.class).actualizar(pelicula.getId(), null)).withRel("actualizar"));
        }

        return model;
    }
}