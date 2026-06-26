package com.cine.reservas.client;

import com.cine.reservas.dto.PeliculaExisteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-pelicula")
public interface PeliculaCliente {

    @GetMapping("/api/peliculas/{id}/existe")
    PeliculaExisteDTO validarPelicula(@PathVariable("id") Long id);
}