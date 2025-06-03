package org.backend.backendfacilgim.repository;

import org.backend.backendfacilgim.entity.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad {@link Serie}.
 * Proporciona métodos para realizar operaciones CRUD y consultas
 * sobre las series asociadas a relaciones entrenamientos-ejercicios.
 *
 * @autor Francisco Santana
 */
@Repository
public interface SerieRepository extends JpaRepository<Serie, Integer> {

    /**
     * Obtiene todas las series asociadas a una relación específica
     * entre entrenamiento y ejercicio.
     *
     * @param entrenamientoEjercicioId ID de la relación EntrenamientoEjercicio.
     * @return Lista de {@link Serie} correspondientes a la relación indicada.
     */
    List<Serie> findByEntrenamientoEjercicio_Id(Integer entrenamientoEjercicioId);

    /**
     * Elimina todas las series vinculadas a una relación específica
     * entre entrenamiento y ejercicio.
     *
     * @param entrenamientoEjercicioId ID de la relación EntrenamientoEjercicio.
     */
    void deleteByEntrenamientoEjercicio_Id(Integer entrenamientoEjercicioId);
}
