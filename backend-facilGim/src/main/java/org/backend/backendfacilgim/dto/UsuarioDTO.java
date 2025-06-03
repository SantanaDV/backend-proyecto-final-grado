package org.backend.backendfacilgim.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la información de un usuario para transferir datos
 * entre cliente y servidor, incluyendo credenciales, información personal
 * y rol de administrador.
 *
 * Autor: Francisco Santana
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    /**
     * Identificador único del usuario.
     * <p>
     * Puede ser nulo cuando se crea un nuevo usuario.
     * </p>
     */
    private Integer idUsuario;

    /**
     * Nombre de usuario para autenticación.
     * <p>
     * Campo obligatorio: no puede estar vacío ni en blanco.
     * </p>
     */
    @NotBlank
    private String username;

    /**
     * Contraseña del usuario.
     * <p>
     * Campo obligatorio: no puede estar vacío ni en blanco.
     * </p>
     */
    @NotBlank
    private String password;

    /**
     * Nombre real del usuario.
     * <p>
     * Campo obligatorio: no puede estar vacío ni en blanco.
     * </p>
     */
    @NotBlank
    private String nombre;

    /**
     * Apellido del usuario.
     * <p>
     * Campo obligatorio: no puede estar vacío ni en blanco.
     * </p>
     */
    @NotBlank
    private String apellido;

    /**
     * Correo electrónico del usuario.
     * <p>
     * Debe tener formato válido de email.
     * </p>
     */
    @Email
    private String correo;

    /**
     * Dirección física o de residencia del usuario.
     * <p>
     * Campo opcional.
     * </p>
     */
    private String direccion;

    /**
     * Indicador de si el usuario tiene rol de administrador.
     */
    private boolean admin;
}
