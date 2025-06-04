package org.backend.backendfacilgim.controller;

import jakarta.validation.Valid;
import org.backend.backendfacilgim.utilities.Utils;
import org.backend.backendfacilgim.dto.EntrenamientoDTO;
import org.backend.backendfacilgim.entity.Entrenamiento;
import org.backend.backendfacilgim.service.EntrenamientoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gestionar operaciones CRUD de entrenamientos.
 * <p>
 * Expone endpoints para:
 * <ul>
 *   <li>Obtener todos los entrenamientos o filtrarlos por fechas, usuario o nombre.</li>
 *   <li>Crear, actualizar (por ID o nombre) y eliminar entrenamientos.</li>
 * </ul>
 * </p>
 *
 * Autor: Francisco Santana
 */
@RestController
@RequestMapping("/api/entrenamientos")
public class EntrenamientoController {

    private final EntrenamientoService entrenamientoService;

    /**
     * Constructor que inyecta el servicio de entrenamientos.
     *
     * @param entrenamientoService Servicio que contiene la lógica de negocio de entrenamientos.
     */
    public EntrenamientoController(EntrenamientoService entrenamientoService) {
        this.entrenamientoService = entrenamientoService;
    }

    /**
     * Obtiene la lista completa de entrenamientos.
     *
     * @return ResponseEntity con la lista de {@link Entrenamiento} y estado 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<Entrenamiento>> obtenerTodosLosEntrenamiento() {
        List<Entrenamiento> lista = entrenamientoService.obtenerTodosLosEntrenamientos();
        return ResponseEntity.ok(lista);
    }

    /**
     * Obtiene los entrenamientos cuya fecha está entre los parámetros {@code fechaInicio} y {@code fechaFin}.
     *
     * @param fechaInicio Fecha inicial (inclusive) en formato ISO (yyyy-MM-dd).
     * @param fechaFin    Fecha final (inclusive) en formato ISO (yyyy-MM-dd).
     * @return ResponseEntity con la lista de {@link Entrenamiento} filtrados y estado 200 OK.
     */
    @GetMapping("/fecha")
    public ResponseEntity<List<Entrenamiento>> obtenerEntrenamientosEntreDosFechas(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin")    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Entrenamiento> lista = entrenamientoService.obtenerEntrenamientosEntreDosFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(lista);
    }

    /**
     * Obtiene un entrenamiento por su identificador.
     *
     * @param id Identificador del entrenamiento.
     * @return ResponseEntity con el {@link Entrenamiento} si existe (200 OK),
     *         o 404 Not Found si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Entrenamiento> obtenerEntrenamientoPorId(@PathVariable Integer id) {
        return entrenamientoService.obtenerEntrenamientoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene la lista de entrenamientos de un usuario específico.
     *
     * @param id Identificador del usuario.
     * @return ResponseEntity con la lista de {@link Entrenamiento} y estado 200 OK.
     */
    @GetMapping("/usuarioId/{id}")
    public ResponseEntity<List<Entrenamiento>> obtenerEntrenamientosUsuario(@PathVariable Integer id) {
        List<Entrenamiento> lista = entrenamientoService.encontrarEntrenamientoPorIdUsuario(id);
        return ResponseEntity.ok(lista);
    }

    /**
     * Obtiene la lista de entrenamientos cuyo nombre coincide con el parámetro.
     *
     * @param nombre Nombre (o parte de él) de los entrenamientos a buscar.
     * @return ResponseEntity con la lista de {@link Entrenamiento} y estado 200 OK.
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Entrenamiento>> obtenerEntrenamientosPorNombre(@PathVariable String nombre) {
        List<Entrenamiento> lista = entrenamientoService.obtenerEntrenamientosPorNombre(nombre);
        return ResponseEntity.ok(lista);
    }

    /**
     * Crea un nuevo entrenamiento a partir de un {@link EntrenamientoDTO}.
     * <p>
     * Valida el DTO y, si contiene errores, retorna una respuesta de validación.
     * En caso contrario, crea el entrenamiento y devuelve el recurso creado.
     * </p>
     *
     * @param dto    Objeto {@link EntrenamientoDTO} con los datos del nuevo entrenamiento.
     * @param result BindingResult que contiene errores de validación del DTO.
     * @return ResponseEntity con el {@link Entrenamiento} creado y estado 201 Created,
     *         o un ResponseEntity de validación en caso de error.
     */
    @PostMapping
    public ResponseEntity<?> crearEntrenamiento(
            @Valid @RequestBody EntrenamientoDTO dto,
            BindingResult result) {
        if (result.hasErrors()) {
            return Utils.validation(result);
        }
        Entrenamiento entrenamiento = entrenamientoService.crearDesdeDTO(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(entrenamiento);
    }

    /**
     * Actualiza un entrenamiento existente identificado por {@code id}.
     * <p>
     * Valida el objeto {@link Entrenamiento} recibido y, si contiene errores,
     * retorna una respuesta de validación. En caso contrario, actualiza y retorna
     * el objeto modificado.
     * </p>
     *
     * @param id           Identificador del entrenamiento a actualizar.
     * @param entrenamiento Objeto {@link Entrenamiento} con los datos a modificar.
     * @param result       BindingResult que contiene errores de validación.
     * @return ResponseEntity con el {@link Entrenamiento} actualizado y estado 200 OK,
     *         o un ResponseEntity de validación en caso de error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEntrenamiento(
            @PathVariable Integer id,
            @Valid @RequestBody Entrenamiento entrenamiento,
            BindingResult result) {
        if (result.hasErrors()) {
            return Utils.validation(result);
        }
        Entrenamiento actualizado = entrenamientoService.actualizarEntrenamiento(id, entrenamiento);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Actualiza un entrenamiento existente buscándolo por su nombre.
     * <p>
     * Valida el objeto {@link Entrenamiento} recibido y si contiene errores,
     * retorna una respuesta de validación. En caso contrario, actualiza el
     * primer entrenamiento que coincide con el nombre y retorna el objeto modificado.
     * </p>
     *
     * @param nombre        Nombre del entrenamiento a actualizar.
     * @param entrenamiento Objeto {@link Entrenamiento} con los datos a modificar.
     * @param result        BindingResult que contiene errores de validación.
     * @return ResponseEntity con el {@link Entrenamiento} actualizado y estado 200 OK,
     *         o un ResponseEntity de validación en caso de error.
     */
    @PutMapping("/nombre/{nombre}")
    public ResponseEntity<?> actualizarEntrenamientoPorNombre(
            @PathVariable String nombre,
            @Valid @RequestBody Entrenamiento entrenamiento,
            BindingResult result) {
        if (result.hasErrors()) {
            return Utils.validation(result);
        }
        Entrenamiento actualizado = entrenamientoService.actualizarEntrenamientoPorNombre(nombre, entrenamiento);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Actualiza un entrenamiento existente usando un DTO {@link EntrenamientoDTO}.
     * <p>
     * Valida el DTO y, si contiene errores, retorna una respuesta de validación.
     * En caso contrario, actualiza el entrenamiento y retorna el objeto modificado.
     * </p>
     *
     * @param id    Identificador del entrenamiento a actualizar.
     * @param dto   Objeto {@link EntrenamientoDTO} con los nuevos datos.
     * @param result BindingResult que contiene errores de validación del DTO.
     * @return ResponseEntity con el {@link Entrenamiento} actualizado y estado 200 OK,
     *         o un ResponseEntity de validación en caso de error.
     */
    @PutMapping("/dto/{id}")
    public ResponseEntity<?> actualizarEntrenamientoDesdeDTO(
            @PathVariable Integer id,
            @Valid @RequestBody EntrenamientoDTO dto,
            BindingResult result) {
        if (result.hasErrors()) {
            return Utils.validation(result);
        }
        Entrenamiento actualizado = entrenamientoService.actualizarEntrenamientoDesdeDTO(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Elimina un entrenamiento por su identificador.
     *
     * @param id Identificador del entrenamiento a eliminar.
     * @return ResponseEntity con estado 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEntrenamiento(@PathVariable Integer id) {
        entrenamientoService.eliminarEntrenamiento(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina un entrenamiento por su nombre.
     *
     * @param nombre Nombre del entrenamiento a eliminar.
     * @return ResponseEntity con estado 204 No Content.
     */
    @DeleteMapping("/nombre/{nombre}")
    public ResponseEntity<Void> eliminarEntrenamientoPorNombre(@PathVariable String nombre) {
        entrenamientoService.eliminarEntrenamientoPorNombre(nombre);
        return ResponseEntity.noContent().build();
    }
}
