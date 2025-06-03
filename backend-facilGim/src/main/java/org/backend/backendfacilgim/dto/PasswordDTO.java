package org.backend.backendfacilgim.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para transferir una contraseña, ya sea para validación
 * de la contraseña actual o para actualizarla.
 *
 * Autor: Francisco Santana
 */
@Data
public class PasswordDTO {

    /**
     * Contraseña a validar o nueva contraseña a establecer.
     * <p>
     * Campo obligatorio: no puede estar vacío ni en blanco.
     * </p>
     */
    @NotBlank
    private String password;
}
