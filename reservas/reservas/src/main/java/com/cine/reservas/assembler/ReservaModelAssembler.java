package com.cine.reservas.assembler;

import com.cine.reservas.controller.ReservaController;
import com.cine.reservas.dto.ReservaResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReservaModelAssembler
        implements RepresentationModelAssembler<ReservaResponseDTO, EntityModel<ReservaResponseDTO>> {

    @Override
    public EntityModel<ReservaResponseDTO> toModel(ReservaResponseDTO reserva) {
        EntityModel<ReservaResponseDTO> model = EntityModel.of(reserva,
                linkTo(methodOn(ReservaController.class).obtenerPorId(reserva.getId())).withSelfRel(),
                linkTo(methodOn(ReservaController.class).obtenerTodas()).withRel("reservas"),
                linkTo(methodOn(ReservaController.class).obtenerPorUsuario(reserva.getUsuario_id())).withRel("reservas-usuario"),
                linkTo(methodOn(ReservaController.class).obtenerPorFuncion(reserva.getFuncion_id())).withRel("reservas-funcion")
        );

        // Cross-service: enlace directo al microservicio de pagos
        model.add(Link.of("/api/pagos/reserva/" + reserva.getId()).withRel("pago"));

        // Acciones disponibles según el estado actual de la reserva
        if ("PENDIENTE".equals(reserva.getEstado())) {
            model.add(linkTo(methodOn(ReservaController.class).confirmar(reserva.getId())).withRel("confirmar"));
            model.add(linkTo(methodOn(ReservaController.class).cancelar(reserva.getId())).withRel("cancelar"));
        } else if ("CONFIRMADA".equals(reserva.getEstado())) {
            model.add(linkTo(methodOn(ReservaController.class).cancelar(reserva.getId())).withRel("cancelar"));
        }

        return model;
    }
}