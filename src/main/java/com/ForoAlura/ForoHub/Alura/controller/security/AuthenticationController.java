package com.ForoAlura.ForoHub.Alura.controller.security;

import com.ForoAlura.ForoHub.Alura.domain.usuario.Usuario;
import com.ForoAlura.ForoHub.Alura.infra.security.AutenticacionUsuarioDTO;
import com.ForoAlura.ForoHub.Alura.controller.security.JWTtokenDTO;
import com.ForoAlura.ForoHub.Alura.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<JWTtokenDTO> autenticarUsuario(@RequestBody @Valid AutenticacionUsuarioDTO datos) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(datos.correoElectronico(), datos.contrasena());
        var usuarioAutenticado = authenticationManager.authenticate(authToken);
        var JWTtoken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
        return ResponseEntity.ok(new JWTtokenDTO(JWTtoken));
    }
}