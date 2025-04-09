package org.backend.backendfacilgim.repository;

import org.backend.backendfacilgim.entity.TipoEntrenamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoEntrenamientoRepository extends JpaRepository<TipoEntrenamiento, Long> {
    boolean existsByNombre(String nombre);
}