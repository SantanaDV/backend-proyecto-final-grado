package org.backend.backendfacilgim.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.backend.backendfacilgim.dto.*;
import org.backend.backendfacilgim.entity.Ejercicio;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.service.EjercicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * Controlador REST para gestionar el catálogo de ejercicios y sus instancias en entrenamientos.
 * <p>
 * Proporciona endpoints para:
 * <ul>
 *   <li>Listar, crear, actualizar y eliminar ejercicios del catálogo global.</li>
 *   <li>Asignar, actualizar y eliminar instancias de ejercicio en un entrenamiento.</li>
 * </ul>
 * </p>
 *
 * Autor: Francisco Santana
 */
@RestController
@RequestMapping("/api/ejercicios")
public class EjercicioController {

    private final EjercicioService ejercicioService;
    private final ObjectMapper objectMapper;

    /**
     * Constructor que inyecta el servicio de ejercicios y crea el ObjectMapper.
     *
     * @param ejercicioService Servicio que maneja la lógica de negocio de ejercicios.
     */
    public EjercicioController(EjercicioService ejercicioService) {
        this.ejercicioService = ejercicioService;
        this.objectMapper = new ObjectMapper();
    }

    // --- CATÁLOGO GLOBAL ---

    /**
     * Obtiene la lista completa de ejercicios del catálogo.
     *
     * @return ResponseEntity con lista de {@link Ejercicio} y estado 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<Ejercicio>> listarCatalogo() {
        List<Ejercicio> lista = ejercicioService.listarEjercicios();
        return ResponseEntity.ok(lista);
    }

    /**
     * Obtiene un ejercicio del catálogo por su ID.
     *
     * @param id Identificador del ejercicio a buscar.
     * @return ResponseEntity con el {@link Ejercicio} si existe (200 OK),
     *         o 404 Not Found si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ejercicio> obtenerCatalogoPorId(@PathVariable Integer id) {
        return ejercicioService.getEjercicio(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo ejercicio en el catálogo o actualiza uno existente.
     * <p>
     * Recibe un JSON en el campo "ejercicio" y opcionalmente un archivo de imagen
     * en el campo "imagen" dentro de una petición multipart/form-data.
     * </p>
     *
     * @param ejercicioJson Cadena JSON que representa el objeto {@link Ejercicio}.
     * @param imagen        Archivo de imagen asociado; puede ser nulo o vacío.
     * @return ResponseEntity con el {@link Ejercicio} creado o actualizado y estado 201 Created,
     *         o 400 Bad Request si faltan campos obligatorios.
     * @throws IOException En caso de error al procesar el archivo de imagen.
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> crearOActualizarCatalogo(
            @RequestPart("ejercicio") String ejercicioJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen
    ) throws IOException {
        if (imagen != null && !imagen.isEmpty()) {
            System.out.println(">>> LLEGÓ fichero: originalFilename = "
                    + imagen.getOriginalFilename()
                    + ", contentType = " + imagen.getContentType()
                    + ", size = " + imagen.getSize());
        } else {
            System.out.println(">>> NO llegó imagen o está vacía");
        }
        Ejercicio datos = objectMapper.readValue(ejercicioJson, Ejercicio.class);

        if (datos.getNombre() == null || datos.getNombre().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre es obligatorio.");
        }

        // Gestión de imagen: guarda y asigna URL si se proporciona
        if (imagen != null && !imagen.isEmpty()) {
            datos.setImagenUrl(guardarImagen(imagen));
        }

        Ejercicio resultado;
        if (datos.getIdEjercicio() != null) {
            // Actualización de un ejercicio existente
            resultado = ejercicioService.actualizarEjercicio(datos.getIdEjercicio(), datos);
        } else {
            // Creación de un nuevo ejercicio
            resultado = ejercicioService.crearEjercicio(datos);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    /**
     * Elimina un ejercicio del catálogo por su ID.
     *
     * @param id Identificador del ejercicio a eliminar.
     * @return ResponseEntity con estado 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCatalogo(@PathVariable Integer id) {
        ejercicioService.eliminarEjercicio(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina un ejercicio del catálogo por su nombre.
     * <p>
     * Recibe un DTO {@link EjercicioDeleteDTO} con el nombre y el usuario propietario.
     * </p>
     *
     * @param datos DTO que contiene {@code nombre} del ejercicio y {@code usernamePropietario}.
     * @return ResponseEntity con estado 204 No Content.
     */
    @DeleteMapping("/nombre")
    public ResponseEntity<Void> eliminarPorNombre(
            @Valid @RequestBody EjercicioDeleteDTO datos
    ) {
        ejercicioService.eliminarEjercicioPorNombre(datos.getNombre(), datos.getUsernamePropietario());
        return ResponseEntity.noContent().build();
    }

    // --- INSTANCIAS EN ENTRENAMIENTOS ---

    /**
     * Asigna un ejercicio del catálogo a un entrenamiento, incluyendo sus series y orden.
     *
     * @param idEntrenamiento ID del entrenamiento al que se asigna el ejercicio.
     * @param body            DTO {@link AsignacionConSeriesDTO} con {@code ejercicioId}, lista de {@code serieDTO} y {@code orden}.
     * @return ResponseEntity con el {@link EjercicioDTO} asignado y estado 201 Created.
     */
    @PostMapping("/entrenamiento/{idEntrenamiento}/asignar")
    public ResponseEntity<EjercicioDTO> asignarAEentrenamientoConSeries(
            @PathVariable Integer idEntrenamiento,
            @Valid @RequestBody AsignacionConSeriesDTO body
    ) {
        EjercicioDTO dto = ejercicioService.asignarEjercicioConSeriesAEntrenamiento(
                idEntrenamiento,
                body.getEjercicioId(),
                body.getSeries(),
                body.getOrden()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Actualiza los valores de peso, repeticiones y orden de una instancia ya asignada
     * de un ejercicio en un entrenamiento.
     *
     * @param relId ID de la relación ejercicio–entrenamiento a actualizar.
     * @param body  DTO {@link ActualizacionInstanciaDTO} con la nueva lista de {@code SerieDTO} y {@code orden}.
     * @return ResponseEntity con el {@link EjercicioDTO} actualizado y estado 200 OK.
     */
    @PutMapping("/entrenamiento/ejercicio/{relId}")
    public ResponseEntity<EjercicioDTO> actualizarInstancia(
            @PathVariable Integer relId,
            @Valid @RequestBody ActualizacionInstanciaDTO body
    ) {
        EjercicioDTO dto = ejercicioService.actualizarInstanciaConSeries(
                relId,
                body.getSeries(),
                body.getOrden()
        );
        return ResponseEntity.ok(dto);
    }

    /**
     * Elimina una instancia (peso/repeticiones) de un ejercicio asignado a un entrenamiento.
     *
     * @param relId ID de la relación ejercicio–entrenamiento a eliminar.
     * @return ResponseEntity con estado 204 No Content.
     */
    @DeleteMapping("/entrenamiento/ejercicio/{relId}")
    public ResponseEntity<Void> eliminarInstancia(@PathVariable Integer relId) {
        ejercicioService.eliminarInstancia(relId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Guarda el archivo de imagen en el sistema de archivos local y devuelve su URL accesible.
     * <p>
     * - Crea el directorio "uploads" si no existe.
     * - Genera un nombre único basado en la marca de tiempo.
     * - Copia el contenido del MultipartFile al directorio.
     * - Construye la URL pública usando el contexto de la aplicación.
     * </p>
     *
     * @param imagen MultipartFile con la imagen a almacenar.
     * @return Cadena con la URL pública para acceder a la imagen guardada.
     * @throws IOException Si ocurre un error al leer o escribir el archivo.
     */
    private String guardarImagen(MultipartFile imagen) throws IOException {
        Path uploadDir = Paths.get("uploads/");
        Files.createDirectories(uploadDir); // crea el directorio si no existe

        String filename = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
        Path path = uploadDir.resolve(filename);
        Files.copy(imagen.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return baseUrl + "/uploads/" + filename;
    }

}
