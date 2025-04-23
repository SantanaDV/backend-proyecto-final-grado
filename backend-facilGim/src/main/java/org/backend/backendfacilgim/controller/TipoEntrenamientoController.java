package org.backend.backendfacilgim.controller;

import jakarta.validation.Valid;
import org.backend.backendfacilgim.Utilities.Utils;
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

@RestController
@RequestMapping("/api/tipos-entrenamiento")
public class TipoEntrenamientoController {

    private final TipoEntrenamientoService service;

    public TipoEntrenamientoController(TipoEntrenamientoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TipoEntrenamientoDTO>> listar() {
        List<TipoEntrenamientoDTO> tipos = service.listarTipos().stream()
                .map(TipoEntrenamientoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tipos);
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody TipoEntrenamientoDTO dto, BindingResult result) {
        if (result.hasErrors()) return Utils.validation(result);
        TipoEntrenamiento creado = service.crearTipo(TipoEntrenamientoMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(TipoEntrenamientoMapper.toDTO(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @Valid @RequestBody TipoEntrenamientoDTO dto,
                                        BindingResult result) {
        if (result.hasErrors()) return Utils.validation(result);
        TipoEntrenamiento actualizado = service.actualizarTipo(id, TipoEntrenamientoMapper.toEntity(dto));
        return ResponseEntity.ok(TipoEntrenamientoMapper.toDTO(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarTipo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoEntrenamientoDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(TipoEntrenamientoMapper.toDTO(service.obtenerPorId(id)));
    }
}
