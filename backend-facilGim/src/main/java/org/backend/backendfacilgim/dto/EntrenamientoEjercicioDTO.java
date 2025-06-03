package org.backend.backendfacilgim.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO que representa la relación entre un Entrenamiento y un Ejercicio,
 * incluyendo la información del ejercicio, su posición (orden) en la sesión
 * y la lista de series (repeticiones/peso) asociadas.
 *
 * Autor: Francisco Santana
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrenamientoEjercicioDTO {

    /**
     * Datos completos del ejercicio asignado al entrenamiento.
     * <p>
     * Campo obligatorio: no puede ser nulo.
     * </p>
     */
    @NotNull
    private EjercicioDTO ejercicio;

    /**
     * Posición u orden en el que aparece este ejercicio dentro de la rutina.
     * <p>
     * Se utiliza para determinar el orden de ejecución en el entrenamiento.
     * </p>
     */
    private int orden;

    /**
     * Lista de objetos {@link SerieDTO} que representan las series (repeticiones y peso)
     * asociadas a este ejercicio dentro del entrenamiento.
     * <p>
     * Puede estar vacía si no se han definido series para este ejercicio.
     * </p>
     */
    private List<SerieDTO> series;
}
