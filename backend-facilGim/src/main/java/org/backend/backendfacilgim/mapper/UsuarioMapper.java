package org.backend.backendfacilgim.mapper;

import lombok.experimental.UtilityClass;
import org.backend.backendfacilgim.dto.UsuarioDTO;
import org.backend.backendfacilgim.entity.Usuario;

/**
 * Mapper de utilidad para convertir entre la entidad {@link Usuario}
 * y su DTO {@link UsuarioDTO}.
 *
 * @autor Francisco Santana
 */
@UtilityClass
public class UsuarioMapper {

    /**
     * Convierte una entidad {@link Usuario} a su DTO equivalente.
     * <p>
     * Este método mapea los campos básicos (id, username, correo, nombre,
     * apellido, dirección) y calcula el flag "admin" comprobando si el usuario
     * tiene asignado el rol "ROLE_ADMIN".
     *
     * @param usuario Entidad de usuario a convertir.
     * @return Un {@link UsuarioDTO} con todos los datos mapeados.
     */
    public static UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setUsername(usuario.getUsername());
        dto.setCorreo(usuario.getCorreo());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setDireccion(usuario.getDireccion());

        // ← Aquí calculamos admin a partir de los roles
        boolean isAdmin = usuario.getRoles().stream()
                .anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));
        dto.setAdmin(isAdmin);

        return dto;
    }

    /**
     * Convierte un {@link UsuarioDTO} a la entidad {@link Usuario}.
     * <p>
     * Este método mapea los campos básicos (username, password, correo,
     * nombre, apellido, dirección). El flag "admin" se copia a un campo
     * transitorio; los roles reales deben gestionarse de forma separada.
     *
     * @param dto DTO de usuario que se desea convertir.
     * @return Una nueva entidad {@link Usuario} con los valores del DTO.
     */
    public static Usuario toEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword());
        usuario.setCorreo(dto.getCorreo());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setDireccion(dto.getDireccion());
        // Este boolean sólo se usa para lógica interna al crear/actualizar,
        // pero el verdadero admin se mantiene en la lista de roles.
        usuario.setAdmin(dto.isAdmin());
        return usuario;
    }
}
