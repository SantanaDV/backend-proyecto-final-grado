package org.backend.backendfacilgim.repository;

import org.backend.backendfacilgim.entity.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad {@link Ejercicio}.
 * Proporciona operaciones CRUD y métodos personalizados
 * para acceder y manipular ejercicios en la base de datos.
 *
 * @autor Francisco Santana
 */
@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, Integer> {

    /**
     * Busca un ejercicio por su nombre único.
     *
     * @param nombre Nombre del ejercicio a buscar.
     * @return El {@link Ejercicio} encontrado, o {@code null} si no existe.
     */
    Ejercicio findByNombre(String nombre);

    /**
     * Elimina un ejercicio de la base de datos utilizando su nombre.
     *
     * @param nombre Nombre del ejercicio a eliminar.
     */
    void deleteByNombre(String nombre);

}
