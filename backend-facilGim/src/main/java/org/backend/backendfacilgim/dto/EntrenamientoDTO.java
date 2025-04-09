package org.backend.backendfacilgim.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO utilizado para recibir datos en el POST de entrenamiento.
 */
@Data
public class EntrenamientoDTO {

    @NotBlank(message = "El nombre del entrenamiento es obligatorio")
    private String nombre;

    @NotNull(message = "La fecha del entrenamiento es obligatoria")
    private LocalDate fechaEntrenamiento;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private int duracion;

    @NotNull(message = "El tipo de entrenamiento es obligatorio")
    private Long tipoEntrenamientoId;

    @NotNull(message = "El usuario es obligatorio")
    private Integer usuarioId;

    @NotEmpty(message = "Debe haber al menos un ejercicio")
    private List<@NotNull Integer> ejerciciosId;
}
