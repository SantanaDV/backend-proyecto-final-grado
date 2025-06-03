package org.backend.backendfacilgim.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de respuesta para el proceso de login.
 * <p>
 * Contiene mensaje informativo, token JWT, nombre de usuario,
 * roles (authorities) y el ID del usuario autenticado.
 * </p>
 *
 * Autor: Francisco Santana
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    /**
     * Mensaje informativo sobre el resultado del login.
     */
    private String mensaje;

    /**
     * Token JWT que se debe enviar en encabezados Authorization para
     * acceder a endpoints protegidos.
     */
    private String token;

    /**
     * Nombre de usuario del usuario autenticado.
     */
    private String username;

    /**
     * Lista de roles o autoridades asignadas al usuario con formato de cadena.
     */
    private List<String> authorities;

    /**
     * Identificador único del usuario en el sistema.
     */
    private Integer userId;
}
