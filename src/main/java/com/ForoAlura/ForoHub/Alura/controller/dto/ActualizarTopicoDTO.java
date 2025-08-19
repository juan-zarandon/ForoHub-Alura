package com.ForoAlura.ForoHub.Alura.controller.dto;
public record ActualizarTopicoDTO(
        String titulo,
        String mensaje,
        Long cursoId
) {}