package org.backend.backendfacilgim.repository;

import org.backend.backendfacilgim.entity.Entrenamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EntrenamientoRepository extends JpaRepository<Entrenamiento, Integer> {

    /**
     * Busca un entrenamiento por nombre
     * @param nombre
     * @return
     */
    List<Entrenamiento> findEntrenamientosByNombre(String nombre);

    List<Entrenamiento> findByFechaEntrenamientoBetween(LocalDate fechaInicio, LocalDate fechaFin);


}
