package org.backend.backendfacilgim.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO que representa un ejercicio del catálogo, incluyendo su identificador,
 * nombre, URL de imagen y lista de series asociadas para una instancia de entrenamiento.
 *
 * Autor: Francisco Santana
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EjercicioDTO {

    /**
     * Identificador único del ejercicio en el catálogo.
     */
    private Integer idEjercicio;

    /**
     * Nombre descriptivo del ejercicio.
     */
    private String nombre;

    /**
     * URL de la imagen representativa del ejercicio.
     */
    private String imagenUrl;

    /**
     * Lista de objetos {@link SerieDTO} que contienen información de repeticiones y peso
     * asociadas a este ejercicio dentro de un entrenamiento.
     * <p>
     * Puede estar vacía si no se ha asignado ninguna serie.
     * </p>
     */
    private List<SerieDTO> series;
}
