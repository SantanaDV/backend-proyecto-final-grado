package org.backend.backendfacilgim.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para eliminar un ejercicio del catálogo por nombre,
 * indicando también el username del propietario que realiza la acción.
 *
 * Autor: Francisco Santana
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EjercicioDeleteDTO {

    /**
     * Nombre del ejercicio a eliminar.
     * <p>
     * Campo obligatorio: no puede estar vacío ni en blanco.
     * </p>
     */
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    /**
     * Username del propietario o usuario que solicita la eliminación.
     * <p>
     * Campo obligatorio: no puede estar vacío ni en blanco.
     * </p>
     */
    @NotBlank(message = "El username del propietario no puede estar vacío")
    private String usernamePropietario;
}
