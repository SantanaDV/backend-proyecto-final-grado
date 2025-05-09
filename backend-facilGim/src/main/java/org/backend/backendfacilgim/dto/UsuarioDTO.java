package org.backend.backendfacilgim.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Integer idUsuario;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @Email
    private String correo;

    private String direccion;

    private boolean admin;
}
