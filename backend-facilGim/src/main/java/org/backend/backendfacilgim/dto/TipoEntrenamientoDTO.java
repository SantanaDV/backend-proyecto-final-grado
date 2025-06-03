package org.backend.backendfacilgim.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un tipo de entrenamiento.
 * <p>
 * Se utiliza para transferir información de los tipos de entrenamiento
 * entre el cliente y el servidor.
 * </p>
 *
 * Autor: Francisco Santana
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoEntrenamientoDTO {

    /**
     * Identificador único del tipo de entrenamiento.
     * <p>
     * Puede ser nulo cuando se crea un nuevo tipo.
     * </p>
     */
    private Long id;

    /**
     * Nombre descriptivo del tipo de entrenamiento.
     * <p>
     * Campo obligatorio: no puede estar vacío ni en blanco.
     * </p>
     */
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
}
