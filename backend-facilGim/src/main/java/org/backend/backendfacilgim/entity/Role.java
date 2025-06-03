package org.backend.backendfacilgim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entidad que representa un rol de usuario en el sistema.
 * Un {@code Role} define permisos/grupos como "ROLE_USER" o "ROLE_ADMIN".
 *
 * Autor: Francisco Santana
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    /**
     * Identificador único del rol.
     * <p>
     * Se genera automáticamente en la base de datos (IDENTITY).
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Nombre único del rol.
     * <p>
     * Por ejemplo: "ROLE_USER", "ROLE_ADMIN". No puede ser nulo ni duplicado.
     * </p>
     */
    @Column(unique = true, nullable = false)
    private String name;

}
