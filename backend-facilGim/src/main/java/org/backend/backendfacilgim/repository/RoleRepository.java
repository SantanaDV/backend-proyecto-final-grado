package org.backend.backendfacilgim.repository;

import org.backend.backendfacilgim.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Busca un rol por su nombre (ej: "ROLE_USER", "ROLE_ADMIN").
     * @param name nombre del rol buscado.
     * @return un Optional con el Role si existe, o vacío si no se encuentra.
     */
    Optional<Role> findByName(String name);
}
