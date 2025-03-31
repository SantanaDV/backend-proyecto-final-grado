package org.backend.backendfacilgim.controller;

import org.backend.backendfacilgim.entity.Ejercicio;
import org.backend.backendfacilgim.service.EjercicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ejercicios")
public class EjercicioController {

    private final EjercicioService ejercicioService;

    public EjercicioController(EjercicioService ejercicioService) {
        this.ejercicioService = ejercicioService;
    }

    /**
     * GET /api/ejercicios
     * Obtiene la lista de todos los ejercicios.
     */
    @GetMapping
    public ResponseEntity<List<Ejercicio>> listarEjercicios() {
        List<Ejercicio> ejercicios = ejercicioService.listarEjercicios();
        return ResponseEntity.ok(ejercicios);
    }

    /**
     * GET /api/ejercicios/{id}
     * Obtiene un ejercicio por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ejercicio> obtenerEjercicioPorId(@PathVariable Integer id) {
        Optional<Ejercicio> ejercicioOpt = ejercicioService.getEjercicio(id);
        return ejercicioOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * GET /api/ejercicios/nombre/{nombre}
     * Obtiene un ejercicio por su nombre.
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Ejercicio> obtenerEjercicioPorNombre(@PathVariable String nombre) {
        Optional<Ejercicio> ejercicioOpt = ejercicioService.getEjercicioByNombre(nombre);
        return ejercicioOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * POST /api/ejercicios
     * Crea un nuevo ejercicio.
     */
    @PostMapping
    public ResponseEntity<Ejercicio> crearEjercicio(@RequestBody Ejercicio ejercicio) {
        Ejercicio nuevo = ejercicioService.crearEjercicio(ejercicio);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    /**
     * PUT /api/ejercicios/{id}
     * Actualiza un ejercicio por su ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ejercicio> actualizarEjercicio(
            @PathVariable Integer id,
            @RequestBody Ejercicio datosNuevos
    ) {
        Ejercicio actualizado = ejercicioService.actualizarEjercicio(id, datosNuevos);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * PUT /api/ejercicios/nombre/{nombre}?username=usuario
     * Actualiza un ejercicio buscándolo por nombre y asociándolo al username proporcionado.
     */
    @PutMapping("/nombre/{nombre}")
    public ResponseEntity<Ejercicio> actualizarEjercicioPorNombre(
            @PathVariable String nombre,
            @RequestBody Ejercicio datosNuevos,
            @RequestParam String username
    ) {
        Ejercicio actualizado = ejercicioService.actualizarEjercicioPorNombre(nombre, datosNuevos, username);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * DELETE /api/ejercicios/{id}
     * Elimina un ejercicio por su ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEjercicio(@PathVariable Integer id) {
        ejercicioService.eliminarEjercicio(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/ejercicios/nombre/{nombre}
     * Elimina un ejercicio por su nombre.
     */
    @DeleteMapping("/nombre/{nombre}")
    public ResponseEntity<Void> eliminarEjercicioPorNombre(@PathVariable String nombre) {
        ejercicioService.eliminarEjercicioPorNombre(nombre);
        return ResponseEntity.noContent().build();
    }
}
