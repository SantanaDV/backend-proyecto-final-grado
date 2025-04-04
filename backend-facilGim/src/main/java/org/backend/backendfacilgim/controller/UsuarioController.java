package org.backend.backendfacilgim.controller;

import jakarta.validation.Valid;
import org.backend.backendfacilgim.entity.Usuario;
import org.backend.backendfacilgim.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.backend.backendfacilgim.Utilities.Utils.validation;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * GET /api/usuarios
     * Lista todos los usuarios.
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    /**
     * GET /api/usuarios/{id}
     * Retorna un usuario por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioService.getUsuario(id);
        return (usuario != null)
                ? ResponseEntity.ok(usuario)
                : ResponseEntity.notFound().build();
    }

    /**
     * GET /api/usuarios/username/{username}
     * Retorna un usuario buscándolo por su username.
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<Usuario> getUsuarioByUsername(@PathVariable String username) {
        Usuario usuario = usuarioService.getUsuarioByUsername(username);
        return (usuario != null)
                ? ResponseEntity.ok(usuario)
                : ResponseEntity.notFound().build();
    }

    /**
     * POST /api/usuarios
     * Crea un nuevo usuario.
     */
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        Usuario creado = usuarioService.crearUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * Registra o crea un nuevo usuario de forma pública.
     * Fuerza admin=false para que nadie se dé de alta como administrador.
     *
     * @param usuario Objeto Usuario con los datos (JSON).
     * @param result BindingResult para validaciones.
     * @return Respuesta con el usuario creado o errores de validación.
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody Usuario usuario,
                                              BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }

        // Forzamos para que no pueda autoproclamarse admin
        usuario.setAdmin(false);


        // Comprobamos username duplicado
        if (usuarioService.existePorUsername(usuario.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El username ya está en uso.");
        }
        if(usuario.getPassword().)

        Usuario creado = usuarioService.crearUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * PUT /api/usuarios/{id}
     * Actualiza los datos de un usuario basándonos en su id.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Integer id,
                                                     @RequestBody Usuario datosNuevos) {
        Usuario actualizado = usuarioService.actualizarUsuario(id, datosNuevos);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * PUT /api/usuarios/username/{username}
     * Actualiza los datos de un usuario basándonos en su username.
     */
    @PutMapping("/username/{username}")
    public ResponseEntity<Usuario> actualizarUsuarioPorUsername(
            @PathVariable String username,
            @RequestBody Usuario datosNuevos
    ) {
        Usuario actualizado = usuarioService.actualizarUsuarioPorUsuario(username, datosNuevos);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * DELETE /api/usuarios/{id}
     * Elimina un usuario por su id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/usuarios/username/{username}
     * Elimina un usuario por su username.
     */
    @DeleteMapping("/username/{username}")
    public ResponseEntity<Void> eliminarUsuarioPorUsername(@PathVariable String username) {
        usuarioService.eliminarUsuarioPorUsername(username);
        return ResponseEntity.noContent().build();
    }
}
