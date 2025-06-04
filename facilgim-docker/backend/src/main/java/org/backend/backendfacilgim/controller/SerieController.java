package org.backend.backendfacilgim.controller;

import jakarta.validation.Valid;
import org.backend.backendfacilgim.utilities.Utils;
import org.backend.backendfacilgim.dto.SerieDTO;
import org.backend.backendfacilgim.entity.Serie;
import org.backend.backendfacilgim.mapper.SerieMapper;
import org.backend.backendfacilgim.service.SerieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar las operaciones CRUD de series asociadas
 * a la relación entre entrenamiento y ejercicio.
 * <p>
 * Proporciona endpoints para:
 * <ul>
 *   <li>Listar series de un entrenamiento-ejercicio específico.</li>
 *   <li>Crear nuevas series.</li>
 *   <li>Actualizar series existentes.</li>
 *   <li>Eliminar y obtener series por ID.</li>
 * </ul>
 * </p>
 *
 * Autor: Francisco Santana
 */
@RestController
@RequestMapping("/api/series")
public class SerieController {

    private final SerieService serieService;

    /**
     * Constructor que inyecta el servicio de series.
     *
     * @param serieService Servicio que maneja la lógica de negocio de {@link Serie}.
     */
    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }

    /**
     * Lista todas las series asociadas a una relación entrenamiento-ejercicio.
     *
     * @param id Identificador de la relación entrenamiento-ejercicio.
     * @return ResponseEntity con lista de {@link SerieDTO} y estado 200 OK.
     */
    @GetMapping("/entrenamiento-ejercicio/{id}")
    public ResponseEntity<List<SerieDTO>> listarPorEntrenamientoEjercicio(@PathVariable Integer id) {
        List<SerieDTO> series = serieService.listarPorEntrenamientoEjercicio(id)
                .stream()
                .map(SerieMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(series);
    }

    /**
     * Crea una nueva serie a partir de un {@link SerieDTO}.
     * <p>
     * Valida el DTO; si contiene errores, retorna una respuesta de validación.
     * Si es válido, crea la entidad y devuelve el DTO resultante con estado 201 Created.
     * </p>
     *
     * @param serieDTO Objeto DTO con los datos de la serie a crear.
     * @param result   BindingResult que contiene errores de validación.
     * @return ResponseEntity con el {@link SerieDTO} creado y estado 201 Created,
     *         o un ResponseEntity de validación en caso de error.
     */
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody SerieDTO serieDTO, BindingResult result) {
        if (result.hasErrors()) {
            return Utils.validation(result);
        }
        Serie serie = SerieMapper.toEntity(serieDTO);
        Serie creada = serieService.crear(serie);
        return ResponseEntity.status(HttpStatus.CREATED).body(SerieMapper.toDTO(creada));
    }

    /**
     * Actualiza una serie existente identificado por {@code id}.
     * <p>
     * Valida el DTO; si contiene errores, retorna una respuesta de validación.
     * Si es válido, actualiza la entidad y devuelve el DTO resultante con estado 200 OK.
     * </p>
     *
     * @param id       Identificador de la serie a actualizar.
     * @param serieDTO Objeto DTO con los datos a actualizar.
     * @param result   BindingResult que contiene errores de validación.
     * @return ResponseEntity con el {@link SerieDTO} actualizado y estado 200 OK,
     *         o un ResponseEntity de validación en caso de error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody SerieDTO serieDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            return Utils.validation(result);
        }
        Serie datos = SerieMapper.toEntity(serieDTO);
        Serie actualizada = serieService.actualizar(id, datos);
        return ResponseEntity.ok(SerieMapper.toDTO(actualizada));
    }

    /**
     * Elimina una serie por su identificador.
     *
     * @param id Identificador de la serie a eliminar.
     * @return ResponseEntity con estado 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        serieService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene una serie por su identificador.
     *
     * @param id Identificador de la serie.
     * @return ResponseEntity con el {@link SerieDTO} correspondiente y estado 200 OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SerieDTO> obtenerPorId(@PathVariable Integer id) {
        Serie serie = serieService.obtenerPorId(id);
        return ResponseEntity.ok(SerieMapper.toDTO(serie));
    }
}
