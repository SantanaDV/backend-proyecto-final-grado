package org.backend.backendfacilgim.repository;

import org.backend.backendfacilgim.entity.TipoEntrenamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad {@link TipoEntrenamiento}.
 * Proporciona métodos para realizar operaciones CRUD y consultas
 * sobre los tipos de entrenamiento.
 *
 * @autor Francisco Santana
 */
@Repository
public interface TipoEntrenamientoRepository extends JpaRepository<TipoEntrenamiento, Long> {

    /**
     * Comprueba si ya existe un tipo de entrenamiento con el nombre dado.
     *
     * @param nombre Nombre del tipo de entrenamiento a verificar.
     * @return {@code true} si existe un registro con ese nombre, {@code false} en caso contrario.
     */
    boolean existsByNombre(String nombre);
}
