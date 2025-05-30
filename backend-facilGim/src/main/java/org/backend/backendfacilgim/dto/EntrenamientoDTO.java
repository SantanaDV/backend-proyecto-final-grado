package org.backend.backendfacilgim.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO utilizado para recibir datos en el POST de entrenamiento.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntrenamientoDTO {

    @NotBlank(message = "El nombre del entrenamiento es obligatorio")
    private String nombre;

    @NotNull(message = "La fecha del entrenamiento es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaEntrenamiento;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private int duracion;

    @NotNull(message = "El tipo de entrenamiento es obligatorio")
    private TipoEntrenamientoDTO tipoEntrenamiento;

    @NotNull(message = "El usuario es obligatorio")
    private UsuarioDTO usuario;

    @NotEmpty(message = "Debe haber al menos un ejercicio")
    private List<@NotNull Integer> ejerciciosId;
    private List<EntrenamientoEjercicioDTO> entrenamientosEjercicios;
}
