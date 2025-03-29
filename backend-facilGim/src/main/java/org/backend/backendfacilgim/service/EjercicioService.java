package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.entity.Ejercicio;

import java.util.List;
import java.util.Optional;

public interface EjercicioService {

    //GET

    /**
     * Retorna un Ejercicio por el Id;
     *
     * @param ejercicioId
     * @return Un objeto Ejercicio
     */
    Optional<Ejercicio> getEjercicio(Integer ejercicioId);


    /**
     * Retorna un ejercicio buscandolo por el nombre del ejercicio
     *
     * @param nombreEjercicio
     * @return Un objeto Ejercicio
     */
    Ejercicio getEjercicioByNombre(String nombreEjercicio);

    /**
     * Lista todos los ejercicios
     *
     * @return List<Ejercicio>
     */
    List<Ejercicio> listarEjercicios();
    //POST

    /**
     * Retorna un objeto ejercicio que ha sido introducio mediante un Json
     *
     * @param ejercicio
     * @return objeto Ejercicio
     */
    Ejercicio crearEjercicio(Ejercicio ejercicio);


    //PUT

    /**
     * Actualiza los datos de un ejercicio en base a su id
     *
     * @param ejercicioId
     * @param datosNuevos
     * @return
     */
    Ejercicio actualizarEjercicio(Integer ejercicioId, Ejercicio datosNuevos);

    /**
     * Actualiza los datos de un ejercicio en base a su nombre
     *
     * @param nombreEjercicio
     * @param datosNuevos
     * @return
     */
    Ejercicio actualizarEjercicioPorNombre(String nombreEjercicio, Ejercicio datosNuevos);


    //DELETE

    /**
     * Eliminar un ejercicio por su id
     *
     * @param id
     */
    void eliminarEjercicio(Integer id);

    /**
     * Eliminar un ejercicio por su nombre
     * @param nombre
     */
    void eliminarEjercicioPorNombre(String nombre);


}
