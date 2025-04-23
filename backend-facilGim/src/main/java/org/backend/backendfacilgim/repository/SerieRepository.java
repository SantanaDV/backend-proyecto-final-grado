package org.backend.backendfacilgim.repository;

import org.backend.backendfacilgim.entity.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Integer> {

    List<Serie> findByEntrenamientoEjercicio_Id(Integer entrenamientoEjercicioId);

    void deleteByEntrenamientoEjercicio_Id(Integer entrenamientoEjercicioId);
}
