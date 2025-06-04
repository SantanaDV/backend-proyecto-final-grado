package org.backend.backendfacilgim.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO utilizado para recibir y transferir los datos de un entrenamiento
 * en las operaciones de creación o actualización.
 * <p>
 * Contiene la información básica del entrenamiento, como nombre, fecha,
 * descripción, duración, tipo, usuario propietario y la lista de ejercicios
 * asociados mediante sus identificadores o instancias completas.
 * </p>
 *
 * Autor: Francisco Santana
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntrenamientoDTO {

    /**
     * Nombre del entrenamiento.
     * <p>
     * Campo obligatorio: no puede estar vacío ni en blanco.
     * </p>
     */
    @NotBlank(message = "El nombre del entrenamiento es obligatorio")
    private String nombre;

    /**
     * Fecha en que se realizará el entrenamiento.
     * <p>
     * Formato JSON esperado: "yyyy-MM-dd".
     * Campo obligatorio: no puede ser nulo.
     * </p>
     */
    @NotNull(message = "La fecha del entrenamiento es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaEntrenamiento;

    /**
     * Descripción opcional del entrenamiento.
     * <p>
     * Puede incluir detalles adicionales sobre la rutina.
     * </p>
     */
    private String descripcion;

    /**
     * Duración estimada del entrenamiento en minutos.
     * <p>
     * Debe ser un valor entero mayor o igual a 1.
     * </p>
     */
    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private int duracion;

    /**
     * Información del tipo de entrenamiento (por ejemplo, fuerza, cardio, etc.).
     * <p>
     * Campo obligatorio: no puede ser nulo. Se espera un objeto {@link TipoEntrenamientoDTO}.
     * </p>
     */
    @NotNull(message = "El tipo de entrenamiento es obligatorio")
    private TipoEntrenamientoDTO tipoEntrenamiento;

    /**
     * Datos del usuario propietario del entrenamiento.
     * <p>
     * Campo obligatorio: no puede ser nulo. Se espera un objeto {@link UsuarioDTO}.
     * </p>
     */
    @NotNull(message = "El usuario es obligatorio")
    private UsuarioDTO usuario;

    /**
     * Lista de identificadores de ejercicios que componen el entrenamiento.
     * <p>
     * Campo obligatorio: debe contener al menos un elemento y cada ID no puede ser nulo.
     * </p>
     */
    @NotEmpty(message = "Debe haber al menos un ejercicio")
    private List<@NotNull Integer> ejerciciosId;

    /**
     * Lista de objetos {@link EntrenamientoEjercicioDTO} que describen en detalle
     * cada ejercicio asignado al entrenamiento, incluyendo series y orden.
     * <p>
     * Este campo puede enviarse vacío o nulo al crear; el servicio lo completará
     * o se utilizará para representar las relaciones completas en respuestas.
     * </p>
     */
    private List<EntrenamientoEjercicioDTO> entrenamientosEjercicios;
}
