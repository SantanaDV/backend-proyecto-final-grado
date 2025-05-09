package org.backend.backendfacilgim.mapper;

import lombok.experimental.UtilityClass;
import org.backend.backendfacilgim.dto.UsuarioDTO;
import org.backend.backendfacilgim.entity.Usuario;

@UtilityClass
public class UsuarioMapper {

    public static UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setUsername(usuario.getUsername());
        dto.setCorreo(usuario.getCorreo());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setDireccion(usuario.getDireccion());
        dto.setAdmin(usuario.isAdmin());
        return dto;
    }

    public static Usuario toEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword());
        usuario.setCorreo(dto.getCorreo());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setDireccion(dto.getDireccion());
        usuario.setAdmin(dto.isAdmin());
        return usuario;
    }
}
