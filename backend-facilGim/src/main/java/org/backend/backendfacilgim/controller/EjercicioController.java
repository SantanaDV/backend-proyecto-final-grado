package org.backend.backendfacilgim.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.backend.backendfacilgim.dto.AsignacionDTO;
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

    // --- CATÁLOGO GLOBAL ---

    @GetMapping
    public ResponseEntity<List<Ejercicio>> listarCatalogo() {
        return ResponseEntity.ok(ejercicioService.listarEjercicios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ejercicio> obtenerCatalogoPorId(@PathVariable Integer id) {
        return ejercicioService.getEjercicio(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> crearOActualizarCatalogo(
            @RequestPart("ejercicio") String ejercicioJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen
    ) throws IOException {

        Ejercicio datos = objectMapper.readValue(ejercicioJson, Ejercicio.class);

        if (datos.getNombre() == null || datos.getNombre().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre es obligatorio.");
        }

        // gestión de imagen
        if (imagen != null && !imagen.isEmpty()) {
            datos.setImagenUrl(guardarImagen(imagen));
        }

        Ejercicio resultado;
        if (datos.getIdEjercicio() != null) {
            // actualización
            resultado = ejercicioService.actualizarEjercicio(datos.getIdEjercicio(), datos);
        } else {
            // creación
            resultado = ejercicioService.crearEjercicio(datos);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCatalogo(@PathVariable Integer id) {
        ejercicioService.eliminarEjercicio(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/nombre")
    public ResponseEntity<Void> eliminarPorNombre(
            @Valid @RequestBody EjercicioDeleteDTO datos
    ) {
        ejercicioService.eliminarEjercicioPorNombre(datos.getNombre(), datos.getUsernamePropietario());
        return ResponseEntity.noContent().build();
    }

    // --- INSTANCIAS EN ENTRENAMIENTOS ---

    /**
     * Asigna un ejercicio del catálogo a un entrenamiento con sus datos específicos.
     */
    @PostMapping("/entrenamiento/{idEntrenamiento}/asignar")
    public ResponseEntity<EjercicioDTO> asignarAEentrenamiento(
            @PathVariable Integer idEntrenamiento,
            @Valid @RequestBody AsignacionDTO body
    ) {
        EjercicioDTO dto = ejercicioService.asignarEjercicioAEntrenamiento(
                idEntrenamiento,
                body.getEjercicioId(),
                body.getPeso(),
                body.getRepeticiones(),
                body.getOrden()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Actualiza peso/repeticiones/orden de una instancia ya asignada.
     */
    @PutMapping("/entrenamiento/ejercicio/{relId}")
    public ResponseEntity<EjercicioDTO> actualizarInstancia(
            @PathVariable Integer relId,
            @Valid @RequestBody AsignacionDTO body
    ) {
        EjercicioDTO dto = ejercicioService.actualizarInstancia(
                relId,
                body.getPeso(),
                body.getRepeticiones(),
                body.getOrden()
        );
        return ResponseEntity.ok(dto);
    }

    /**
     * Elimina la instancia (peso/reps) de un ejercicio en un entrenamiento.
     */
    @DeleteMapping("/entrenamiento/ejercicio/{relId}")
    public ResponseEntity<Void> eliminarInstancia(@PathVariable Integer relId) {
        ejercicioService.eliminarInstancia(relId);
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
