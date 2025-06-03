package org.backend.backendfacilgim.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO utilizado para asignar un ejercicio del catálogo a un entrenamiento,
 * incluyendo las series (repeticiones/peso) y el orden dentro del entrenamiento.
 * <p>
 * Este objeto se envía en el body de la petición para crear la relación
 * entre un ejercicio y su entrenamiento, junto con las series asociadas.
 * </p>
 *
 * Autor: Francisco Santana
 */
public class AsignacionConSeriesDTO {

    /**
     * Identificador del ejercicio a asignar.
     * <p>
     * Campo obligatorio: no puede ser nulo.
     * </p>
     */
    @NotNull
    private Integer ejercicioId;

    /**
     * Posición u orden del ejercicio dentro del entrenamiento.
     * <p>
     * Si se omite, el servicio puede asignar un orden por defecto (por ejemplo, al final).
     * </p>
     */
    private Integer orden;

    /**
     * Lista de objetos {@link SerieDTO} que representan las series (repeticiones y peso)
     * que se asignarán a este ejercicio dentro del entrenamiento.
     * <p>
     * Campo obligatorio: no puede ser nulo ni vacío.
     * </p>
     */
    @NotNull
    private List<SerieDTO> series;

    /**
     * Obtiene el identificador del ejercicio a asignar.
     *
     * @return el ID del ejercicio; nunca nulo si se cumple la validación.
     */
    public Integer getEjercicioId() {
        return ejercicioId;
    }

    /**
     * Establece el identificador del ejercicio a asignar.
     *
     * @param ejercicioId ID del ejercicio; no debe ser nulo.
     */
    public void setEjercicioId(Integer ejercicioId) {
        this.ejercicioId = ejercicioId;
    }

    /**
     * Obtiene el orden o posición deseada para el ejercicio dentro del entrenamiento.
     *
     * @return el índice (orden) dentro del entrenamiento, o {@code null} si no se especificó.
     */
    public Integer getOrden() {
        return orden;
    }

    /**
     * Establece el orden o posición del ejercicio dentro del entrenamiento.
     *
     * @param orden índice (posición) deseada; puede ser nulo para usar el valor por defecto.
     */
    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    /**
     * Obtiene la lista de {@link SerieDTO} asociadas a este ejercicio.
     *
     * @return lista de series; nunca es nula si se cumple la validación.
     */
    public List<SerieDTO> getSeries() {
        return series;
    }

    /**
     * Establece la lista de {@link SerieDTO} para el ejercicio.
     *
     * @param series lista de series; no debe ser nula ni vacía.
     */
    public void setSeries(List<SerieDTO> series) {
        this.series = series;
    }
}
