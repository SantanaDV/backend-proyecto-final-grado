package org.backend.backendfacilgim.controller;

import org.backend.backendfacilgim.entity.TipoEntrenamiento;
import org.backend.backendfacilgim.service.TipoEntrenamientoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-entrenamiento")
public class TipoEntrenamientoController {

    private final TipoEntrenamientoService service;

    public TipoEntrenamientoController(TipoEntrenamientoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TipoEntrenamiento>> listar() {
        return ResponseEntity.ok(service.listarTipos());
    }

    @PostMapping
    public ResponseEntity<TipoEntrenamiento> crear(@RequestBody TipoEntrenamiento tipo) {
        return ResponseEntity.ok(service.crearTipo(tipo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoEntrenamiento> actualizar(@PathVariable Long id, @RequestBody TipoEntrenamiento tipo) {
        return ResponseEntity.ok(service.actualizarTipo(id, tipo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        service.eliminarTipo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoEntrenamiento> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
}