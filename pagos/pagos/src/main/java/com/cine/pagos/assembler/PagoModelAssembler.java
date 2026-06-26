package com.cine.pagos.assembler;

import com.cine.pagos.controller.PagoController;
import com.cine.pagos.dto.PagoResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PagoModelAssembler
        implements RepresentationModelAssembler<PagoResponseDTO, EntityModel<PagoResponseDTO>> {

    @Override
    public EntityModel<PagoResponseDTO> toModel(PagoResponseDTO pago) {
        EntityModel<PagoResponseDTO> model = EntityModel.of(pago,
                linkTo(methodOn(PagoController.class).obtenerPorId(pago.getId())).withSelfRel(),
                linkTo(methodOn(PagoController.class).obtenerPorReserva(pago.getReservaId())).withRel("pago-por-reserva")
        );

        // Cross-service: enlace directo al microservicio de reservas
        model.add(Link.of("/api/reservas/" + pago.getReservaId()).withRel("reserva"));

        return model;
    }
}