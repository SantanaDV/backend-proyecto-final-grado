package org.backend.backendfacilgim.controller;

import jakarta.validation.Valid;
import org.backend.backendfacilgim.Utilities.Utils;
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

@RestController
@RequestMapping("/api/series")
public class SerieController {

    private final SerieService serieService;

    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }

    @GetMapping("/entrenamiento-ejercicio/{id}")
    public ResponseEntity<List<SerieDTO>> listarPorEntrenamientoEjercicio(@PathVariable Integer id) {
        List<SerieDTO> series = serieService.listarPorEntrenamientoEjercicio(id)
                .stream()
                .map(SerieMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(series);
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody SerieDTO serieDTO, BindingResult result) {
        if (result.hasErrors()) return Utils.validation(result);
        Serie serie = SerieMapper.toEntity(serieDTO);
        Serie creada = serieService.crear(serie);
        return ResponseEntity.status(HttpStatus.CREATED).body(SerieMapper.toDTO(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody SerieDTO serieDTO, BindingResult result) {
        if (result.hasErrors()) return Utils.validation(result);
        Serie datos = SerieMapper.toEntity(serieDTO);
        Serie actualizada = serieService.actualizar(id, datos);
        return ResponseEntity.ok(SerieMapper.toDTO(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        serieService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SerieDTO> obtenerPorId(@PathVariable Integer id) {
        Serie serie = serieService.obtenerPorId(id);
        return ResponseEntity.ok(SerieMapper.toDTO(serie));
    }
}
