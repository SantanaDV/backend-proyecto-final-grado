package org.backend.backendfacilgim.controller;

import jakarta.validation.Valid;
import org.backend.backendfacilgim.dto.EntrenamientoDTO;
import org.backend.backendfacilgim.entity.Entrenamiento;
import org.backend.backendfacilgim.service.EntrenamientoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/entrenamientos")
public class EntrenamientoController {

    private final EntrenamientoService entrenamientoService;

    public EntrenamientoController(EntrenamientoService entrenamientoService) {
        this.entrenamientoService = entrenamientoService;
    }

    /**
     * GET /api/entrenamientos
     * Lista todos los entrenamientos.
     */

    @GetMapping
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<List<Entrenamiento>> obtenerTodosLosEntrenamiento() {
        List<Entrenamiento> entrenamientos = entrenamientoService.obtenerTodosLosEntrenamiento();
        return ResponseEntity.ok(entrenamientos);
    }

    /**
     * GET /api/entrenamientos/fecha?fechaInicio=YYYY-MM-DD&fechaFin=YYYY-MM-DD
     * Retorna una lista de entrenamientos entre dos fechas.
     */
    @GetMapping("/fecha")
    public ResponseEntity<List<Entrenamiento>> obtenerEntrenamientosEntreDosFechas(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        List<Entrenamiento> entrenamientos =
                entrenamientoService.obtenerEntrenamientosEntreDosFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(entrenamientos);
    }

    /**
     * GET /api/entrenamientos/{id}
     * Retorna un entrenamiento por su id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Entrenamiento> obtenerEntrenamientoPorId(@PathVariable Integer id) {
        Optional<Entrenamiento> entOpt = entrenamientoService.obtenerEntrenamientoPorId(id);
        return entOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * GET /api/entrenamientos/nombre/{nombre}
     * Retorna una lista de entrenamientos por su nombre.
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Entrenamiento>> obtenerEntrenamientosPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(entrenamientoService.obtenerEntrenamientosPorNombre(nombre));
    }

    /**
     * POST /api/entrenamientos
     * Crea un entrenamiento.
     */
    @PostMapping
    public ResponseEntity<Entrenamiento> crearEntrenamiento(@RequestBody Entrenamiento entrenamiento) {
        Entrenamiento creado = entrenamientoService.crearEntrenamiento(entrenamiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * PUT /api/entrenamientos/{id}
     * Actualiza un entrenamiento basándonos en su id.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Entrenamiento> actualizarEntrenamiento(
            @PathVariable Integer id,
            @RequestBody Entrenamiento entrenamiento
    ) {
        Entrenamiento actualizado = entrenamientoService.actualizarEntrenamiento(id, entrenamiento);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * PUT /api/entrenamientos/nombre/{nombre}
     * Actualiza un entrenamiento basándonos en su nombre.
     */
    @PutMapping("/nombre/{nombre}")
    public ResponseEntity<Entrenamiento> actualizarEntrenamientoPorNombre(
            @PathVariable String nombre,
            @RequestBody Entrenamiento entrenamiento
    ) {
        Entrenamiento actualizado = entrenamientoService.actualizarEntrenamientoPorNombre(nombre, entrenamiento);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * PUT /api/entrenamientos/dto/{id}
     * Actualiza un entrenamiento a partir de un DTO con IDs simples.
     */
    @PutMapping("/dto/{id}")
    public ResponseEntity<Entrenamiento> actualizarEntrenamientoDesdeDTO(
            @PathVariable Integer id,
            @Valid @RequestBody EntrenamientoDTO dto) {
        Entrenamiento actualizado = entrenamientoService.actualizarEntrenamientoDesdeDTO(id, dto);
        return ResponseEntity.ok(actualizado);
    }


    /**
     * DELETE /api/entrenamientos/{id}
     * Elimina un entrenamiento por su id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEntrenamiento(@PathVariable Integer id) {
        entrenamientoService.eliminarEntrenamiento(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/entrenamientos/nombre/{nombre}
     * Elimina un entrenamiento por su nombre.
     */
    @DeleteMapping("/nombre/{nombre}")
    public ResponseEntity<Void> eliminarEntrenamientoPorNombre(@PathVariable String nombre) {
        entrenamientoService.eliminarEntrenamientoPorNombre(nombre);
        return ResponseEntity.noContent().build();
    }
}
