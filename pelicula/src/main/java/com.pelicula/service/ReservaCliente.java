package com.pelicula.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "ms-reservas")
public interface ReservaCliente {

    @GetMapping("/api/reservas/pelicula/{peliculaId}")
    List<Object> obtenerReservasPorPelicula(@PathVariable("peliculaId") Long peliculaId);
}