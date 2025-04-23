// dto/UsuarioRequestDTO.java
package org.backend.backendfacilgim.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

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
}
