package org.backend.backendfacilgim.controller;

import jakarta.validation.Valid;
import org.backend.backendfacilgim.dto.UsuarioDTO;
import org.backend.backendfacilgim.dto.UsuarioRequestDTO;
import org.backend.backendfacilgim.entity.Usuario;
import org.backend.backendfacilgim.mapper.UsuarioMapper;
import org.backend.backendfacilgim.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.backend.backendfacilgim.Utilities.Utils.validation;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.listarUsuarios()
                .stream().map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioService.getUsuario(id);
        return ResponseEntity.ok(UsuarioMapper.toDTO(usuario));
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> getUsuarioByUsername(@PathVariable String username) {
        Usuario usuario = usuarioService.obtenerUsuarioPorUsername(username);
        return ResponseEntity.ok(UsuarioMapper.toDTO(usuario));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> crearUsuario(@RequestBody Usuario usuario) {
        if (usuarioService.existePorUsername(usuario.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario en uso");
        } else {
            usuarioService.crearUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("usuario creado");
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO dto, BindingResult result) {
        if (result.hasErrors()) return validation(result);

        if (usuarioService.existePorUsername(dto.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El username ya está en uso.");
        }

        Usuario nuevo = UsuarioMapper.toEntity(dto);
        nuevo.setAdmin(false); // por seguridad
        Usuario creado = usuarioService.crearUsuario(nuevo);

        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDTO(creado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Integer id,
                                                        @RequestBody Usuario datosNuevos) {
        Usuario actualizado = usuarioService.actualizarUsuario(id, datosNuevos);
        return ResponseEntity.ok(UsuarioMapper.toDTO(actualizado));
    }

    @PutMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> actualizarUsuarioPorUsername(@PathVariable String username,
                                                                   @RequestBody Usuario datosNuevos) {
        Usuario actualizado = usuarioService.actualizarUsuarioPorUsuario(username, datosNuevos);
        return ResponseEntity.ok(UsuarioMapper.toDTO(actualizado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }

    @DeleteMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminarUsuarioPorUsername(@PathVariable String username) {
        usuarioService.eliminarUsuarioPorUsername(username);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
}
