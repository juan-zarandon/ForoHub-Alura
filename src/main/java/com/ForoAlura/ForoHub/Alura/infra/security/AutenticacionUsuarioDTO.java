package com.ForoAlura.ForoHub.Alura.infra.security;

public record AutenticacionUsuarioDTO(
        String correoElectronico,
        String contrasena
) {
}