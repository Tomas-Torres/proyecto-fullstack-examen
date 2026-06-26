package com.usuario.assembler;

import com.usuario.controller.UsuarioController;
import com.usuario.dto.UsuarioResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class  UsuarioModelAssembler
        implements RepresentationModelAssembler<UsuarioResponseDTO, EntityModel<UsuarioResponseDTO>> {

    @Override
    public EntityModel<UsuarioResponseDTO> toModel(UsuarioResponseDTO usuario) {
        EntityModel<UsuarioResponseDTO> model = EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtenerPorId(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).obtenerTodos()).withRel("usuarios"),
                linkTo(methodOn(UsuarioController.class).obtenerActivos()).withRel("usuarios-activos")
        );

        // Cross-service: enlace al historial de reservas del usuario
        model.add(Link.of("/api/reservas/usuario/" + usuario.getId()).withRel("reservas"));

        // Acciones disponibles según estado del usuario
        if (Boolean.TRUE.equals(usuario.getActivo())) {
            model.add(linkTo(methodOn(UsuarioController.class).eliminar(usuario.getId())).withRel("desactivar"));
            model.add(linkTo(methodOn(UsuarioController.class).actualizar(usuario.getId(), null)).withRel("actualizar"));
        }

        return model;
    }
}