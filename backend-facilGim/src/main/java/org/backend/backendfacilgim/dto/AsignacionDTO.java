package org.backend.backendfacilgim.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO para asignar o actualizar un ejercicio en un entrenamiento,
 * con peso, repeticiones y orden dentro de la sesión.
 */
@Data
public class AsignacionDTO {

    @NotNull(message = "El id del ejercicio es obligatorio")
    private Integer ejercicioId;

    @Min(value = 0, message = "El peso no puede ser negativo")
    private double peso;

    @Min(value = 1, message = "Debe haber al menos una repetición")
    private int repeticiones;

    /**
     * Orden opcional dentro del entrenamiento
     */
    private Integer orden;
}
