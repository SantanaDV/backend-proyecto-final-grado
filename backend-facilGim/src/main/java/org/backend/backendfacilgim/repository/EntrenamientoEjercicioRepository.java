package org.backend.backendfacilgim.repository;

import org.backend.backendfacilgim.entity.Entrenamiento;
import org.backend.backendfacilgim.entity.EntrenamientoEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntrenamientoEjercicioRepository extends JpaRepository<EntrenamientoEjercicio, Integer> {

    List<EntrenamientoEjercicio> findByEjercicio_IdEjercicio(Integer idEjercicio);


    List<EntrenamientoEjercicio> findByEntrenamiento_IdEntrenamiento(Integer idEntrenamiento);


    List<EntrenamientoEjercicio> findByEntrenamiento(Entrenamiento entrenamiento);

    Optional<EntrenamientoEjercicio> findByEntrenamiento_IdEntrenamientoAndEjercicio_IdEjercicio(Integer idEntrenamiento, Integer idEjercicio);

}
