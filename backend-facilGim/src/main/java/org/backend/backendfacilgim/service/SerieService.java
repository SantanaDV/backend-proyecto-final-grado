package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.entity.Serie;

import java.util.List;

public interface SerieService {

    /**
     * Lista todas las series asociadas a un entrenamiento-ejercicio.
     *
     * @param entrenamientoEjercicioId ID del registro en la tabla intermedia.
     * @return Lista de series asociadas.
     */
    List<Serie> listarPorEntrenamientoEjercicio(Integer entrenamientoEjercicioId);

    /**
     * Crea una nueva serie para un entrenamiento-ejercicio.
     *
     * @param serie Objeto serie a crear.
     * @return Serie creada.
     */
    Serie crear(Serie serie);

    /**
     * Actualiza una serie existente.
     *
     * @param id    ID de la serie.
     * @param serie Datos actualizados.
     * @return Serie actualizada.
     */
    Serie actualizar(Integer id, Serie serie);

    /**
     * Elimina una serie por su ID.
     *
     * @param id ID de la serie.
     */
    void eliminar(Integer id);

    /**
     * Obtiene una serie por su ID.
     *
     * @param id ID de la serie.
     * @return Serie encontrada.
     */
    Serie obtenerPorId(Integer id);
}
