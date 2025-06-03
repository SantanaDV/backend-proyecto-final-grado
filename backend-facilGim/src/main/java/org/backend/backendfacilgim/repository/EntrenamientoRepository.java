package org.backend.backendfacilgim.repository;

import org.backend.backendfacilgim.entity.Entrenamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para la entidad {@link Entrenamiento}.
 * Proporciona métodos para realizar operaciones CRUD y consultas
 * personalizadas sobre entrenamientos en la base de datos.
 *
 * @autor Francisco Santana
 */
@Repository
public interface EntrenamientoRepository extends JpaRepository<Entrenamiento, Integer> {

    /**
     * Busca entrenamientos cuyo nombre coincide exactamente con el proporcionado.
     *
     * @param nombre Nombre del entrenamiento a buscar.
     * @return Lista de {@link Entrenamiento} con el nombre especificado, o lista vacía si no se encuentran.
     */
    List<Entrenamiento> findEntrenamientosByNombre(String nombre);

    /**
     * Obtiene todos los entrenamientos cuya fecha de entrenamiento
     * está entre las dos fechas proporcionadas (inclusive).
     *
     * @param fechaInicio Fecha de inicio del rango (inclusive).
     * @param fechaFin    Fecha de fin del rango (inclusive).
     * @return Lista de {@link Entrenamiento} dentro del rango de fechas, o lista vacía si no se encuentran.
     */
    List<Entrenamiento> findByFechaEntrenamientoBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Obtiene todos los entrenamientos asociados a un usuario específico.
     *
     * @param usuarioIdUsuario Identificador del usuario.
     * @return Lista de {@link Entrenamiento} que pertenecen al usuario indicado, o lista vacía si no se encuentran.
     */
    List<Entrenamiento> findEntrenamientosByUsuario_IdUsuario(Integer usuarioIdUsuario);

}
