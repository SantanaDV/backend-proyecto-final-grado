package org.backend.backendfacilgim.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.backend.backendfacilgim.dto.EjercicioDTO;
import org.backend.backendfacilgim.dto.EjercicioDeleteDTO;
import org.backend.backendfacilgim.entity.Ejercicio;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.service.EjercicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@RestController
@RequestMapping("/api/ejercicios")
public class EjercicioController {

    private final EjercicioService ejercicioService;
    private final ObjectMapper objectMapper;

    public EjercicioController(EjercicioService ejercicioService) {
        this.ejercicioService = ejercicioService;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping
    public ResponseEntity<List<Ejercicio>> listarEjercicios() {
        return ResponseEntity.ok(ejercicioService.listarEjercicios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ejercicio> obtenerEjercicioPorId(@PathVariable Integer id) {
        return ejercicioService.getEjercicio(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Ejercicio> obtenerEjercicioPorNombre(@PathVariable String nombre) {
        return ejercicioService.getEjercicioByNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/entrenamiento/{idEntrenamiento}")
    public ResponseEntity<List<EjercicioDTO>> listarEjerciciosPorEntrenamiento(
            @PathVariable Integer idEntrenamiento,
            @RequestParam String username) {
        return ResponseEntity.ok(
                ejercicioService.listarEjerciciosPorEntrenamientoYUsuario(idEntrenamiento, username));
    }

    /**
     * POST /api/ejercicios
     * Crea o actualiza un ejercicio, con imagen opcional.
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> crearOActualizarEjercicio(
            @RequestPart("ejercicio") String ejercicioJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen
    ) throws IOException {

        Ejercicio datos = objectMapper.readValue(ejercicioJson, Ejercicio.class);

        if (datos.getNombre() == null || datos.getNombre().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre es obligatorio.");
        }

        Ejercicio resultado;

        if (datos.getIdEjercicio() != null && ejercicioService.getEjercicio(datos.getIdEjercicio()).isPresent()) {
            // Caso actualización
            Ejercicio existente = ejercicioService.getEjercicio(datos.getIdEjercicio())
                    .orElseThrow(() -> new CustomException("Ejercicio no encontrado"));

            // Mantener imagen si no se sube nueva
            if (imagen != null && !imagen.isEmpty()) {
                String nuevaImagen = guardarImagen(imagen);
                datos.setImagenUrl(nuevaImagen);
            } else {
                datos.setImagenUrl(existente.getImagenUrl());
            }

            resultado = ejercicioService.actualizarEjercicio(datos.getIdEjercicio(), datos);
        } else {
            // Caso creación
            if (imagen != null && !imagen.isEmpty()) {
                String nuevaImagen = guardarImagen(imagen);
                datos.setImagenUrl(nuevaImagen);
            }
            resultado = ejercicioService.crearEjercicio(datos);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEjercicio(@PathVariable Integer id) {
        ejercicioService.eliminarEjercicio(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/nombre")
    public ResponseEntity<Void> eliminarEjercicioPorNombre(@Valid @RequestBody EjercicioDeleteDTO datos) {
        ejercicioService.eliminarEjercicioPorNombre(datos.getNombre(), datos.getUsernamePropietario());
        return ResponseEntity.noContent().build();
    }

    private String guardarImagen(MultipartFile imagen) throws IOException {
        Path uploadDir = Paths.get("src/main/resources/static/uploads/");
        Files.createDirectories(uploadDir);

        String filename = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
        Path path = uploadDir.resolve(filename);
        Files.copy(imagen.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }
}
