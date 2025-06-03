package org.backend.backendfacilgim.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO para la actualización de una instancia de ejercicio-en-entrenamiento.
 * <p>
 * Contiene la nueva posición (orden) dentro del entrenamiento y la lista de series
 * asociadas a dicha instancia.
 * </p>
 *
 * Autor: Francisco Santana
 */
public class ActualizacionInstanciaDTO {

    /**
     * Orden o posición del ejercicio dentro del entrenamiento.
     * No puede ser nulo.
     */
    @NotNull
    private Integer orden;

    /**
     * Lista de objetos {@link SerieDTO} que representan las series (repeticiones y peso)
     * asociadas a esta instancia de ejercicio.
     * No puede ser nula.
     */
    @NotNull
    private List<SerieDTO> series;

    /**
     * Obtiene el orden de la instancia en el entrenamiento.
     *
     * @return el índice (orden) de la instancia, nunca nulo.
     */
    public Integer getOrden() {
        return orden;
    }

    /**
     * Establece el orden de la instancia en el entrenamiento.
     *
     * @param orden índice (posición) dentro del entrenamiento; no puede ser nulo.
     */
    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    /**
     * Obtiene la lista de {@link SerieDTO} asociadas a esta instancia de ejercicio.
     *
     * @return lista de series; nunca nula.
     */
    public List<SerieDTO> getSeries() {
        return series;
    }

    /**
     * Establece la lista de {@link SerieDTO} para esta instancia de ejercicio.
     *
     * @param series lista de series; no puede ser nula.
     */
    public void setSeries(List<SerieDTO> series) {
        this.series = series;
    }
}
