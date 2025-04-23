package org.backend.backendfacilgim.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer idUsuario;
    private String username;
    private String correo;
    private String nombre;
    private String apellido;
}
