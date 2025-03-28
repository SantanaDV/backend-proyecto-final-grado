package org.backend.backendfacilgim.repository;

import org.backend.backendfacilgim.entity.Entrenamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntrenamientoRepository extends JpaRepository<Entrenamiento, Integer> {

    /**
     * Busca un entrenamiento por nombre
     * @param nombre
     * @return
     */
    Entrenamiento findEntrenamientoByNombre(String nombre);



}
