package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.entity.EntrenamientoEjercicio;

import java.util.List;

/**
 * Servicio para gestionar las relaciones entre entrenamientos y ejercicios
 * en la base de datos. Permite crear, obtener y eliminar instancias de
 * {@link EntrenamientoEjercicio}.
 *
 * @author Francisco Santana
 */
public interface EntrenamientoEjercicioService {

    /**
     * Crea una nueva relación entre un entrenamiento y un ejercicio.
     *
     * @param entity Entidad {@link EntrenamientoEjercicio} a persistir, incluyendo
     *               la referencia al {@code Entrenamiento} y al {@code Ejercicio},
     *               así como sus series y orden.
     * @return La entidad persistida con su ID asignado.
     */
    EntrenamientoEjercicio crear(EntrenamientoEjercicio entity);

    /**
     * Obtiene todas las relaciones entre entrenamientos y ejercicios existentes.
     *
     * @return Lista de todas las instancias de {@link EntrenamientoEjercicio}
     *         almacenadas en la base de datos.
     */
    List<EntrenamientoEjercicio> obtenerTodos();

    /**
     * Obtiene la lista de relaciones {@link EntrenamientoEjercicio} asociadas
     * a un entrenamiento específico, ordenadas según el campo 'orden'.
     *
     * @param idEntrenamiento ID del entrenamiento cuyas relaciones se desean consultar.
     * @return Lista de {@link EntrenamientoEjercicio} asociadas a dicho entrenamiento.
     */
    List<EntrenamientoEjercicio> obtenerPorIdEntrenamiento(Integer idEntrenamiento);

    /**
     * Obtiene una única relación entre entrenamiento y ejercicio por su ID.
     *
     * @param id ID de la instancia {@link EntrenamientoEjercicio} a buscar.
     * @return Objeto {@link EntrenamientoEjercicio} si existe, o lanza excepción
     *         si no se encuentra.
     */
    EntrenamientoEjercicio obtenerPorId(Integer id);

    /**
     * Elimina la relación entre un entrenamiento y un ejercicio dado su ID.
     *
     * @param id ID de la instancia {@link EntrenamientoEjercicio} a eliminar.
     */
    void eliminarPorId(Integer id);
}
