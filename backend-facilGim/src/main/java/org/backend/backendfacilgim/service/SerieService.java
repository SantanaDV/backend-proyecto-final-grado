package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.entity.Serie;

import java.util.List;

/**
 * Servicio para la gestión de entidades {@link Serie}.
 * <p>
 * Define operaciones CRUD básicas sobre la entidad Serie,
 * que representa una serie (con repeticiones y peso) asociada a un entrenamiento-ejercicio.
 * </p>
 *
 * @author Francisco Santana
 */
public interface SerieService {

    /**
     * Lista todas las series asociadas a un {@link org.backend.backendfacilgim.entity.EntrenamientoEjercicio}.
     *
     * @param entrenamientoEjercicioId ID del registro en la tabla intermedia (entrenamiento_ejercicio).
     * @return Lista de {@link Serie} asociadas a ese entrenamiento-ejercicio.
     */
    List<Serie> listarPorEntrenamientoEjercicio(Integer entrenamientoEjercicioId);

    /**
     * Crea una nueva {@link Serie} para un entrenamiento-ejercicio.
     *
     * @param serie Objeto {@link Serie} a persistir, debe contener número de serie, repeticiones, peso
     *              y referencia al entrenamiento-ejercicio.
     * @return La {@link Serie} recién creada, con su ID asignado.
     */
    Serie crear(Serie serie);

    /**
     * Actualiza una {@link Serie} existente.
     * <p>
     * Sustituye los valores de la serie (número, repeticiones, peso) por los datos proporcionados.
     * </p>
     *
     * @param id    ID de la {@link Serie} a actualizar.
     * @param serie Objeto {@link Serie} con los datos actualizados.
     * @return La {@link Serie} actualizada.
     */
    Serie actualizar(Integer id, Serie serie);

    /**
     * Elimina una {@link Serie} por su ID.
     *
     * @param id ID de la {@link Serie} a eliminar.
     */
    void eliminar(Integer id);

    /**
     * Obtiene una {@link Serie} por su ID.
     *
     * @param id ID de la {@link Serie} buscada.
     * @return La {@link Serie} encontrada.
     * @throws RuntimeException si no existe ninguna serie con ese ID.
     */
    Serie obtenerPorId(Integer id);
}
