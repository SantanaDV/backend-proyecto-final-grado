package org.backend.backendfacilgim.controller;

import jakarta.validation.Valid;
import org.backend.backendfacilgim.dto.EntrenamientoEjercicioDTO;
import org.backend.backendfacilgim.entity.Entrenamiento;
import org.backend.backendfacilgim.entity.Ejercicio;
import org.backend.backendfacilgim.entity.EntrenamientoEjercicio;
import org.backend.backendfacilgim.mapper.EntrenamientoEjercicioMapper;
import org.backend.backendfacilgim.service.EntrenamientoEjercicioService;
import org.backend.backendfacilgim.service.EntrenamientoService;
import org.backend.backendfacilgim.service.EjercicioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entrenamiento-ejercicio")
public class EntrenamientoEjercicioController {

    private final EntrenamientoEjercicioService entrenamientoEjercicioService;
    private final EntrenamientoService entrenamientoService;
    private final EjercicioService ejercicioService;

    public EntrenamientoEjercicioController(
            EntrenamientoEjercicioService entrenamientoEjercicioService,
            EntrenamientoService entrenamientoService,
            EjercicioService ejercicioService) {
        this.entrenamientoEjercicioService = entrenamientoEjercicioService;
        this.entrenamientoService = entrenamientoService;
        this.ejercicioService = ejercicioService;
    }

    @GetMapping
    public ResponseEntity<List<EntrenamientoEjercicio>> listarTodos() {
        return ResponseEntity.ok(entrenamientoEjercicioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrenamientoEjercicio> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(entrenamientoEjercicioService.obtenerPorId(id));
    }

    /*@PostMapping
    public ResponseEntity<EntrenamientoEjercicio> crear(@Valid @RequestBody EntrenamientoEjercicioDTO dto) {
        Entrenamiento entrenamiento = entrenamientoService.obtenerEntrenamientoPorId(dto.getIdEntrenamiento())
                .orElseThrow(() -> new RuntimeException("Entrenamiento no encontrado"));
        Ejercicio ejercicio = ejercicioService.getEjercicio(dto.getEjercicio().getIdEjercicio())
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado"));

        EntrenamientoEjercicio entity = EntrenamientoEjercicioMapper.fromDTO(dto, entrenamiento, ejercicio);
        return ResponseEntity.ok(entrenamientoEjercicioService.crear(entity));
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        entrenamientoEjercicioService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/entrenamiento/{idEntrenamiento}")
    public ResponseEntity<List<EntrenamientoEjercicio>> buscarPorEntrenamiento(@PathVariable Integer idEntrenamiento) {
        return ResponseEntity.ok(entrenamientoEjercicioService.obtenerPorIdEntrenamiento(idEntrenamiento));
    }
}
