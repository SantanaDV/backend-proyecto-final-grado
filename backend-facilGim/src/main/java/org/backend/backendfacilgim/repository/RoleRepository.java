package org.backend.backendfacilgim.repository;

import org.backend.backendfacilgim.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para la entidad {@link Role}.
 * Proporciona métodos para realizar operaciones CRUD y consultas
 * sobre los roles en la base de datos.
 *
 * @autor Francisco Santana
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Busca un rol por su nombre (ej: "ROLE_USER", "ROLE_ADMIN").
     *
     * @param name Nombre del rol buscado.
     * @return Un {@link Optional} con el {@link Role} si existe, o vacío si no se encuentra.
     */
    Optional<Role> findByName(String name);
}
