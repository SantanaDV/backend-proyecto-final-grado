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

@RestController
@RequestMapping("/api/entrenamientos")
public class EntrenamientoController {

    private final EntrenamientoService entrenamientoService;

    public EntrenamientoController(EntrenamientoService entrenamientoService) {
        this.entrenamientoService = entrenamientoService;
    }

    @GetMapping
    public ResponseEntity<List<Entrenamiento>> obtenerTodosLosEntrenamiento() {
        return ResponseEntity.ok(entrenamientoService.obtenerTodosLosEntrenamientos());
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<Entrenamiento>> obtenerEntrenamientosEntreDosFechas(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(entrenamientoService.obtenerEntrenamientosEntreDosFechas(fechaInicio, fechaFin));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrenamiento> obtenerEntrenamientoPorId(@PathVariable Integer id) {
        return entrenamientoService.obtenerEntrenamientoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/usuarioId/{id}")
    public ResponseEntity<List<Entrenamiento>> obtenerEntrenamientosUsuario(@PathVariable Integer id){
        return ResponseEntity.ok(entrenamientoService.encontrarEntrenamientoPorIdUsuario(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Entrenamiento>> obtenerEntrenamientosPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(entrenamientoService.obtenerEntrenamientosPorNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<?> crearEntrenamiento(
            @Valid @RequestBody EntrenamientoDTO dto,
            BindingResult result) {
        if (result.hasErrors()) return Utils.validation(result);

        Entrenamiento entrenamiento = entrenamientoService.crearDesdeDTO(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(entrenamiento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEntrenamiento(
            @PathVariable Integer id,
            @Valid @RequestBody Entrenamiento entrenamiento,
            BindingResult result) {
        if (result.hasErrors()) return Utils.validation(result);
        Entrenamiento actualizado = entrenamientoService.actualizarEntrenamiento(id, entrenamiento);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/nombre/{nombre}")
    public ResponseEntity<?> actualizarEntrenamientoPorNombre(
            @PathVariable String nombre,
            @Valid @RequestBody Entrenamiento entrenamiento,
            BindingResult result) {
        if (result.hasErrors()) return Utils.validation(result);
        Entrenamiento actualizado = entrenamientoService.actualizarEntrenamientoPorNombre(nombre, entrenamiento);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/dto/{id}")
    public ResponseEntity<?> actualizarEntrenamientoDesdeDTO(
            @PathVariable Integer id,
            @Valid @RequestBody EntrenamientoDTO dto,
            BindingResult result) {
        if (result.hasErrors()) return Utils.validation(result);
        Entrenamiento actualizado = entrenamientoService.actualizarEntrenamientoDesdeDTO(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEntrenamiento(@PathVariable Integer id) {
        entrenamientoService.eliminarEntrenamiento(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/nombre/{nombre}")
    public ResponseEntity<Void> eliminarEntrenamientoPorNombre(@PathVariable String nombre) {
        entrenamientoService.eliminarEntrenamientoPorNombre(nombre);
        return ResponseEntity.noContent().build();
    }
}
