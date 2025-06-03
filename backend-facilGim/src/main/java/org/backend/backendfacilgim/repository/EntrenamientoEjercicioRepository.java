package org.backend.backendfacilgim.repository;

import org.backend.backendfacilgim.entity.Entrenamiento;
import org.backend.backendfacilgim.entity.EntrenamientoEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad {@link EntrenamientoEjercicio}.
 * Proporciona métodos para consultar, agregar y eliminar relaciones
 * entre entrenamientos y ejercicios.
 *
 * @autor Francisco Santana
 */
public interface EntrenamientoEjercicioRepository extends JpaRepository<EntrenamientoEjercicio, Integer> {

    /**
     * Obtiene todas las relaciones que involucran un ejercicio específico.
     *
     * @param idEjercicio Identificador del ejercicio.
     * @return Lista de {@link EntrenamientoEjercicio} asociados al ejercicio.
     */
    List<EntrenamientoEjercicio> findByEjercicio_IdEjercicio(Integer idEjercicio);

    /**
     * Obtiene todas las relaciones de un entrenamiento dado, ordenadas por el campo "orden" de forma ascendente.
     *
     * @param idEntrenamiento Identificador del entrenamiento.
     * @return Lista de {@link EntrenamientoEjercicio} asociados al entrenamiento, ordenados por orden ascendente.
     */
    List<EntrenamientoEjercicio> findByEntrenamiento_IdEntrenamientoOrderByOrdenAsc(Integer idEntrenamiento);

    /**
     * Obtiene todas las relaciones de un objeto {@link Entrenamiento}.
     *
     * @param entrenamiento Entidad de entrenamiento.
     * @return Lista de {@link EntrenamientoEjercicio} asociados al entrenamiento.
     */
    List<EntrenamientoEjercicio> findByEntrenamiento(Entrenamiento entrenamiento);

    /**
     * Busca una relación específica entre un entrenamiento y un ejercicio.
     *
     * @param idEntrenamiento Identificador del entrenamiento.
     * @param idEjercicio     Identificador del ejercicio.
     * @return {@link Optional} con la relación encontrada, o vacío si no existe.
     */
    Optional<EntrenamientoEjercicio> findByEntrenamiento_IdEntrenamientoAndEjercicio_IdEjercicio(
            Integer idEntrenamiento, Integer idEjercicio);

    /**
     * Elimina todas las relaciones asociadas a un entrenamiento dado.
     *
     * @param idEntrenamiento Identificador del entrenamiento.
     */
    void deleteAllByEntrenamiento_IdEntrenamiento(Integer idEntrenamiento);
}
