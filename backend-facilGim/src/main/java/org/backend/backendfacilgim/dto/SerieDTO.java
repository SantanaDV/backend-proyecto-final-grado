package org.backend.backendfacilgim.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa los datos de una serie dentro de un ejercicio
 * en un entrenamiento, incluyendo identificador, número de serie,
 * repeticiones y peso.
 *
 * Autor: Francisco Santana
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SerieDTO {

    /**
     * Identificador único de la serie.
     * <p>
     * Puede ser nulo cuando se está creando una nueva serie.
     * </p>
     */
    private Integer id;

    /**
     * Número de la serie dentro del conjunto de series
     * (por ejemplo, 1 para la primera serie, 2 para la segunda, etc.).
     */
    private Integer numeroSerie;

    /**
     * Cantidad de repeticiones que se deben realizar en esta serie.
     */
    private int repeticiones;

    /**
     * Peso (en kilogramos) utilizado en esta serie.
     */
    private double peso;
}
