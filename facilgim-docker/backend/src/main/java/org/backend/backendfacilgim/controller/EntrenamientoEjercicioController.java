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

/**
 * Controlador REST para gestionar las relaciones entre entrenamientos y ejercicios.
 * <p>
 * Proporciona endpoints para:
 * <ul>
 *   <li>Listar todas las instancias {@link EntrenamientoEjercicio}.</li>
 *   <li>Obtener una instancia por su ID.</li>
 *   <li>Eliminar una instancia por su ID.</li>
 *   <li>Buscar todas las instancias asociadas a un entrenamiento específico.</li>
 * </ul>
 * </p>
 *
 * Autor: Francisco Santana
 */
@RestController
@RequestMapping("/api/entrenamiento-ejercicio")
public class EntrenamientoEjercicioController {

    private final EntrenamientoEjercicioService entrenamientoEjercicioService;
    private final EntrenamientoService entrenamientoService;
    private final EjercicioService ejercicioService;

    /**
     * Constructor que inyecta los servicios necesarios para manejar entidades
     * {@link EntrenamientoEjercicio}, {@link Entrenamiento} y {@link Ejercicio}.
     *
     * @param entrenamientoEjercicioService Servicio de lógica de negocio para EntrenamientoEjercicio.
     * @param entrenamientoService          Servicio de lógica de negocio para Entrenamiento.
     * @param ejercicioService              Servicio de lógica de negocio para Ejercicio.
     */
    public EntrenamientoEjercicioController(
            EntrenamientoEjercicioService entrenamientoEjercicioService,
            EntrenamientoService entrenamientoService,
            EjercicioService ejercicioService) {
        this.entrenamientoEjercicioService = entrenamientoEjercicioService;
        this.entrenamientoService = entrenamientoService;
        this.ejercicioService = ejercicioService;
    }

    /**
     * Lista todas las instancias {@link EntrenamientoEjercicio} existentes.
     *
     * @return ResponseEntity con lista de {@link EntrenamientoEjercicio} y estado 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<EntrenamientoEjercicio>> listarTodos() {
        List<EntrenamientoEjercicio> lista = entrenamientoEjercicioService.obtenerTodos();
        return ResponseEntity.ok(lista);
    }

    /**
     * Obtiene una instancia {@link EntrenamientoEjercicio} por su identificador.
     *
     * @param id ID de la instancia a recuperar.
     * @return ResponseEntity con el {@link EntrenamientoEjercicio} y estado 200 OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntrenamientoEjercicio> obtenerPorId(@PathVariable Integer id) {
        EntrenamientoEjercicio entidad = entrenamientoEjercicioService.obtenerPorId(id);
        return ResponseEntity.ok(entidad);
    }

    /**
     * Elimina una instancia {@link EntrenamientoEjercicio} por su identificador.
     *
     * @param id ID de la instancia a eliminar.
     * @return ResponseEntity con estado 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        entrenamientoEjercicioService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca todas las instancias {@link EntrenamientoEjercicio} asociadas
     * a un entrenamiento específico.
     *
     * @param idEntrenamiento ID del entrenamiento cuyas instancias se desean obtener.
     * @return ResponseEntity con lista de {@link EntrenamientoEjercicio} y estado 200 OK.
     */
    @GetMapping("/entrenamiento/{idEntrenamiento}")
    public ResponseEntity<List<EntrenamientoEjercicio>> buscarPorEntrenamiento(@PathVariable Integer idEntrenamiento) {
        List<EntrenamientoEjercicio> lista = entrenamientoEjercicioService.obtenerPorIdEntrenamiento(idEntrenamiento);
        return ResponseEntity.ok(lista);
    }
}
