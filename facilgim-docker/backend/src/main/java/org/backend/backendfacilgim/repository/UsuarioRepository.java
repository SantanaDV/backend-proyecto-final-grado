package org.backend.backendfacilgim.repository;

import org.backend.backendfacilgim.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interfaz para la persistencia de la entidad {@link Usuario}.
 * Extiende JpaRepository para proporcionar métodos CRUD básicos y la posibilidad de definir consultas personalizadas.
 *
 * @autor Francisco Santana
 */
@Repository // La anotación @Repository indica a Spring que esta interfaz es un componente
// de la capa de persistencia. Esto permite que Spring la inyecte automáticamente en los servicios donde se requiera.
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un {@link Usuario} por su username.
     *
     * @param username el nombre de usuario a buscar
     * @return un Optional que contiene el Usuario si se encuentra, o vacío si no existe
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Busca un {@link Usuario} por su correo electrónico.
     *
     * @param correo el correo del usuario a buscar
     * @return un Optional que contiene el Usuario si se encuentra, o vacío si no existe
     */
    Optional<Usuario> findByCorreo(String correo);

    /**
     * Verifica si ya existe un {@link Usuario} con el username dado.
     *
     * @param username el nombre de usuario a verificar
     * @return {@code true} si existe al menos un usuario con ese username, {@code false} en caso contrario
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe al menos un {@link Usuario} con el rol especificado.
     * Utiliza el nombre del rol para realizar la búsqueda (por ejemplo "ROLE_ADMIN").
     *
     * @param roleName el nombre del rol a comprobar
     * @return {@code true} si hay al menos un usuario con ese rol, {@code false} en caso contrario
     */
    boolean existsByRoles_Name(String roleName);
}
