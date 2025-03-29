package org.backend.backendfacilgim.repository;

import jakarta.validation.constraints.NotNull;
import org.backend.backendfacilgim.entity.Entrenamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface EntrenamientoRepository extends JpaRepository<Entrenamiento, Integer> {

    /**
     * Busca un entrenamiento por nombre
     * @param nombre
     * @return
     */
    Entrenamiento findEntrenamientoByNombre(String nombre);

    List<Entrenamiento> findByFechaEntrenamientoBetween(LocalDate fechaInicio, LocalDate fechaFin);


}
