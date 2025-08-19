package com.ForoAlura.ForoHub.Alura.controller;

import com.ForoAlura.ForoHub.Alura.controller.dto.ActualizarTopicoDTO;
import com.ForoAlura.ForoHub.Alura.controller.dto.CrearTopicoDTO;
import com.ForoAlura.ForoHub.Alura.controller.dto.DetallesTopicoDTO;
import com.ForoAlura.ForoHub.Alura.domain.topico.Topico;
import com.ForoAlura.ForoHub.Alura.domain.topico.TopicoRepository;
import com.ForoAlura.ForoHub.Alura.domain.usuario.Usuario;
import com.ForoAlura.ForoHub.Alura.domain.usuario.UsuarioRepository;
import com.ForoAlura.ForoHub.Alura.domain.curso.Curso;
import com.ForoAlura.ForoHub.Alura.domain.curso.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DetallesTopicoDTO> crearTopico(
            @RequestBody @Valid CrearTopicoDTO crearTopicoDTO,
            UriComponentsBuilder uriComponentsBuilder) {


        Usuario usuario = usuarioRepository.getReferenceById(crearTopicoDTO.usuarioId());
        Curso curso = cursoRepository.getReferenceById(crearTopicoDTO.cursoId());


        Topico topico = new Topico(
                crearTopicoDTO.titulo(),
                crearTopicoDTO.mensaje(),
                usuario.getNombre(),
                curso.getNombre()
        );

        topicoRepository.save(topico);

        URI url = uriComponentsBuilder
                .path("/topicos/{id}")
                .buildAndExpand(topico.getId())
                .toUri();

        return ResponseEntity.created(url).body(new DetallesTopicoDTO(topico));
    }

    @GetMapping
    public ResponseEntity<Page<DetallesTopicoDTO>> listarTopicos(
            @PageableDefault(size = 10) Pageable paginacion) {

        var page = topicoRepository.findByStatusTrue(paginacion)
                .map(DetallesTopicoDTO::new);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallesTopicoDTO> obtenerTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DetallesTopicoDTO(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DetallesTopicoDTO> actualizarTopico(
            @PathVariable Long id,
            @RequestBody @Valid ActualizarTopicoDTO actualizarTopicoDTO) {

        Topico topico = topicoRepository.getReferenceById(id);


        String cursoNombre = null;
        if (actualizarTopicoDTO.cursoId() != null) {
            Curso curso = cursoRepository.getReferenceById(actualizarTopicoDTO.cursoId());
            cursoNombre = curso.getNombre();
        }

        topico.actualizar(
                actualizarTopicoDTO.titulo(),
                actualizarTopicoDTO.mensaje(),
                null // status no está en tu ActualizarTopicoDTO
        );


        if (cursoNombre != null) {
            // Aquí necesitarías un método para actualizar el curso en la entidad
            // topico.actualizarCurso(cursoNombre);
        }

        return ResponseEntity.ok(new DetallesTopicoDTO(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        // En lugar de eliminar físicamente, cambiar status a false
        topico.actualizar(null, null, false);
        return ResponseEntity.noContent().build();
    }
}