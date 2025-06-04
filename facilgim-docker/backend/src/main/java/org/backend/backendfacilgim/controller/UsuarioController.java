package org.backend.backendfacilgim.controller;

import jakarta.validation.Valid;
import org.backend.backendfacilgim.dto.PasswordDTO;
import org.backend.backendfacilgim.dto.UsuarioDTO;
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

import static org.backend.backendfacilgim.utilities.Utils.validation;

/**
 * Controlador REST para gestionar operaciones CRUD sobre usuarios.
 * <p>
 * Proporciona endpoints para:
 * <ul>
 *   <li>Listar usuarios (solo ADMIN).</li>
 *   <li>Obtener usuario por ID o por username.</li>
 *   <li>Crear usuario (ADMIN) o auto-registrarse como usuario normal.</li>
 *   <li>Validar contraseña actual de un usuario.</li>
 *   <li>Actualizar datos de usuario (por ID o por username).</li>
 *   <li>Actualizar contraseña (USER o ADMIN).</li>
 *   <li>Eliminar usuario (por ID o por username, solo ADMIN).</li>
 * </ul>
 * </p>
 *
 * Autor: Francisco Santana
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Constructor que inyecta el servicio de lógica de negocio para usuarios.
     *
     * @param usuarioService Servicio que gestiona operaciones sobre {@link Usuario}.
     */
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Lista todos los usuarios registrados.
     * <p>
     * Solo accesible por usuarios con rol ADMIN.
     * </p>
     *
     * @return ResponseEntity con lista de {@link UsuarioDTO} y estado 200 OK.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.listarUsuarios()
                .stream()
                .map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtiene un usuario por su ID.
     * <p>
     * Accesible por usuarios con rol USER.
     * </p>
     *
     * @param id Identificador del usuario.
     * @return ResponseEntity con {@link UsuarioDTO} y estado 200 OK.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable Integer id) {
        Usuario u = usuarioService.getUsuario(id);
        return ResponseEntity.ok(UsuarioMapper.toDTO(u));
    }

    /**
     * Obtiene un usuario por su username.
     * <p>
     * Solo accesible por usuarios con rol ADMIN.
     * </p>
     *
     * @param username Nombre de usuario (username).
     * @return ResponseEntity con {@link UsuarioDTO} y estado 200 OK.
     */
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> getUsuarioByUsername(@PathVariable String username) {
        Usuario u = usuarioService.obtenerUsuarioPorUsername(username);
        return ResponseEntity.ok(UsuarioMapper.toDTO(u));
    }

    /**
     * Crea un nuevo usuario (solo ADMIN).
     * <p>
     * Recibe un objeto {@link Usuario} con datos sin DTO, verifica que el username no exista,
     * y lo guarda en la base de datos. Retorna mensaje de creación exitosa o error.
     * </p>
     *
     * @param usuario Entidad {@link Usuario} a crear.
     * @return ResponseEntity con mensaje y estado 201 Created, o 400 Bad Request si el username ya existe.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> crearUsuario(@RequestBody Usuario usuario) {
        if (usuarioService.existePorUsername(usuario.getUsername())) {
            return ResponseEntity.badRequest().body("Usuario en uso");
        }
        usuarioService.crearUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body("usuario creado");
    }

    /**
     * Permite el registro de un nuevo usuario (sin necesidad de rol ADMIN).
     * <p>
     * Recibe un {@link UsuarioDTO}, valida sus campos, verifica unicidad de username,
     * y crea un usuario con rol normal (admin=false). Retorna el DTO creado.
     * </p>
     *
     * @param dto    Objeto {@link UsuarioDTO} con los datos del usuario a registrar.
     * @param result BindingResult que contiene errores de validación del DTO.
     * @return ResponseEntity con {@link UsuarioDTO} creado y estado 201 Created,
     *         o respuesta de validación en caso de errores.
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(
            @Valid @RequestBody UsuarioDTO dto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return validation(result);
        }
        if (usuarioService.existePorUsername(dto.getUsername())) {
            return ResponseEntity.badRequest().body("El username ya está en uso.");
        }
        Usuario nuevo = UsuarioMapper.toEntity(dto);
        nuevo.setAdmin(false);
        Usuario creado = usuarioService.crearUsuario(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UsuarioMapper.toDTO(creado));
    }

    /**
     * Valida la contraseña actual de un usuario dado su username.
     * <p>
     * Accesible por roles USER o ADMIN. Recibe un {@link PasswordDTO} con la contraseña pura,
     * y verifica si coincide con la almacenada. Retorna 200 OK si es válida, 401 Unauthorized si falla.
     * </p>
     *
     * @param username Nombre de usuario (username).
     * @param dto      Objeto {@link PasswordDTO} que contiene la contraseña a validar.
     * @return ResponseEntity con estado 200 OK o 401 Unauthorized.
     */
    @PostMapping("/username/{username}/validate-password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> validarPasswordActual(
            @PathVariable String username,
            @Valid @RequestBody PasswordDTO dto
    ) {
        boolean ok = usuarioService.validarPasswordPorUsername(username, dto.getPassword());
        return ok
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * Actualiza un usuario existente por su ID (solo ADMIN).
     * <p>
     * Recibe una entidad {@link Usuario} con los nuevos datos, delega en el servicio y retorna
     * el {@link UsuarioDTO} actualizado.
     * </p>
     *
     * @param id         Identificador del usuario a actualizar.
     * @param datosNuevos Entidad {@link Usuario} con los datos modificados.
     * @return ResponseEntity con {@link UsuarioDTO} actualizado y estado 200 OK.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(
            @PathVariable Integer id,
            @RequestBody Usuario datosNuevos
    ) {
        Usuario actualizado = usuarioService.actualizarUsuario(id, datosNuevos);
        return ResponseEntity.ok(UsuarioMapper.toDTO(actualizado));
    }

    /**
     * Actualiza un usuario existente identificado por su username (solo ADMIN).
     * <p>
     * Recibe una entidad {@link Usuario} con los nuevos datos, busca por username y actualiza,
     * luego retorna el {@link UsuarioDTO} resultante.
     * </p>
     *
     * @param username   Username del usuario a actualizar.
     * @param datosNuevos Entidad {@link Usuario} con los datos modificados.
     * @return ResponseEntity con {@link UsuarioDTO} actualizado y estado 200 OK.
     */
    @PutMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> actualizarUsuarioPorUsername(
            @PathVariable String username,
            @RequestBody Usuario datosNuevos
    ) {
        Usuario actualizado = usuarioService.actualizarUsuarioPorUsuario(username, datosNuevos);
        return ResponseEntity.ok(UsuarioMapper.toDTO(actualizado));
    }

    /**
     * Actualiza la contraseña de un usuario dado su ID.
     * <p>
     * Accesible por roles USER o ADMIN. Recibe un {@link PasswordDTO} con la nueva contraseña pura,
     * valida que el usuario exista, y delega en el servicio para encodear y guardar la nueva contraseña.
     * Retorna el {@link UsuarioDTO} actualizado.
     * </p>
     *
     * @param id  Identificador del usuario.
     * @param dto Objeto {@link PasswordDTO} con la nueva contraseña.
     * @return ResponseEntity con {@link UsuarioDTO} actualizado y estado 200 OK,
     *         o 404 Not Found si el usuario no existe.
     */
    @PutMapping("/password/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> actualizarContraseña(
            @PathVariable Integer id,
            @Valid @RequestBody PasswordDTO dto
    ) {
        // 1) Verifica que el usuario exista
        Usuario existente = usuarioService.getUsuario(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        // 2) Delegar en el servicio para encodear y guardar la contraseña
        Usuario actualizado = usuarioService.actualizarContraseña(id, dto.getPassword());
        return ResponseEntity.ok(UsuarioMapper.toDTO(actualizado));
    }

    /**
     * Elimina un usuario por su ID (solo ADMIN).
     *
     * @param id Identificador del usuario a eliminar.
     * @return ResponseEntity con mensaje de confirmación y estado 200 OK.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }

    /**
     * Elimina un usuario por su username (solo ADMIN).
     *
     * @param username Username del usuario a eliminar.
     * @return ResponseEntity con mensaje de confirmación y estado 200 OK.
     */
    @DeleteMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminarUsuarioPorUsername(@PathVariable String username) {
        usuarioService.eliminarUsuarioPorUsername(username);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
}
