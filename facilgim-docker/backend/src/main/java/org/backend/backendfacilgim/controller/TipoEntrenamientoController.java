package org.backend.backendfacilgim.controller;

import jakarta.validation.Valid;
import org.backend.backendfacilgim.utilities.Utils;
import org.backend.backendfacilgim.dto.TipoEntrenamientoDTO;
import org.backend.backendfacilgim.entity.TipoEntrenamiento;
import org.backend.backendfacilgim.mapper.TipoEntrenamientoMapper;
import org.backend.backendfacilgim.service.TipoEntrenamientoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar los tipos de entrenamiento.
 * <p>
 * Expone endpoints para:
 * <ul>
 *   <li>Listar todos los tipos.</li>
 *   <li>Crear un nuevo tipo.</li>
 *   <li>Actualizar un tipo existente.</li>
 *   <li>Eliminar un tipo por ID.</li>
 *   <li>Obtener un tipo específico por ID.</li>
 * </ul>
 * </p>
 *
 * Autor: Francisco Santana
 */
@RestController
@RequestMapping("/api/tipos-entrenamiento")
public class TipoEntrenamientoController {

    private final TipoEntrenamientoService service;

    /**
     * Constructor que inyecta el servicio de lógica para TipoEntrenamiento.
     *
     * @param service Servicio que maneja la lógica de negocio de {@link TipoEntrenamiento}.
     */
    public TipoEntrenamientoController(TipoEntrenamientoService service) {
        this.service = service;
    }

    /**
     * Lista todos los tipos de entrenamiento disponibles.
     *
     * @return ResponseEntity con la lista de {@link TipoEntrenamientoDTO} y estado 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<TipoEntrenamientoDTO>> listar() {
        List<TipoEntrenamientoDTO> tipos = service.listarTipos().stream()
                .map(TipoEntrenamientoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tipos);
    }

    /**
     * Crea un nuevo tipo de entrenamiento a partir de un DTO.
     * <p>
     * Valida el DTO; si contiene errores, retorna una respuesta de validación.
     * En caso contrario, crea la entidad y devuelve el DTO resultante con estado 201 Created.
     * </p>
     *
     * @param dto    Objeto {@link TipoEntrenamientoDTO} con los datos del nuevo tipo.
     * @param result BindingResult que contiene errores de validación.
     * @return ResponseEntity con el {@link TipoEntrenamientoDTO} creado y estado 201 Created,
     *         o un ResponseEntity de validación en caso de error.
     */
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody TipoEntrenamientoDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return Utils.validation(result);
        }
        TipoEntrenamiento creado = service.crearTipo(TipoEntrenamientoMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(TipoEntrenamientoMapper.toDTO(creado));
    }

    /**
     * Actualiza un tipo de entrenamiento existente identificado por {@code id}.
     * <p>
     * Valida el DTO; si contiene errores, retorna una respuesta de validación.
     * En caso contrario, actualiza la entidad y devuelve el DTO resultante con estado 200 OK.
     * </p>
     *
     * @param id     Identificador del tipo a actualizar.
     * @param dto    Objeto {@link TipoEntrenamientoDTO} con los datos a modificar.
     * @param result BindingResult que contiene errores de validación.
     * @return ResponseEntity con el {@link TipoEntrenamientoDTO} actualizado y estado 200 OK,
     *         o un ResponseEntity de validación en caso de error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TipoEntrenamientoDTO dto,
            BindingResult result) {
        if (result.hasErrors()) {
            return Utils.validation(result);
        }
        TipoEntrenamiento actualizado = service.actualizarTipo(id, TipoEntrenamientoMapper.toEntity(dto));
        return ResponseEntity.ok(TipoEntrenamientoMapper.toDTO(actualizado));
    }

    /**
     * Elimina un tipo de entrenamiento por su identificador.
     *
     * @param id Identificador del tipo a eliminar.
     * @return ResponseEntity con estado 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarTipo(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene un tipo de entrenamiento por su identificador.
     *
     * @param id Identificador del tipo a recuperar.
     * @return ResponseEntity con el {@link TipoEntrenamientoDTO} correspondiente y estado 200 OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoEntrenamientoDTO> obtener(@PathVariable Long id) {
        TipoEntrenamiento tipo = service.obtenerPorId(id);
        return ResponseEntity.ok(TipoEntrenamientoMapper.toDTO(tipo));
    }
}
