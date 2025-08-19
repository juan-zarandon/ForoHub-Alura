package com.ForoAlura.ForoHub.Alura.domain.topico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    private Boolean status = true;
    private String autor;
    private String curso; // Campo añadido para corregir el error


    public Topico(String titulo, String mensaje, String autor, String curso) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.autor = autor;
        this.curso = curso;
    }

    public void actualizar(String titulo, String mensaje, Boolean status) {
        if(titulo != null) {
            this.titulo = titulo;
        }
        if(mensaje != null) {
            this.mensaje = mensaje;
        }
        if(status != null) {
            this.status = status;
        }
    }
}
