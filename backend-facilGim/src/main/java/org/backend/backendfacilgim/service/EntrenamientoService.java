package org.backend.backendfacilgim.service;


import org.backend.backendfacilgim.entity.Entrenamiento;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EntrenamientoService {


    //GET

    /**
     * Lista todos los Etrenamientos
     *
     * @return List<Ejercicio>
     */
    List<Entrenamiento> obtenerTodosLosEntrenamiento();

    /**
     * Retorna una lista de entrenamientos entre dos fechas
     *
     * @param fechaInicio
     * @param fechaFin
     * @return List<Ejercicio>
     */
    List<Entrenamiento> obtenerEntrenamientosEntreDosFechas(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Retorna un entrenamiento por su id
     *
     * @param id
     * @return objeto Entrenamiento
     */
    Optional<Entrenamiento> obtenerEntrenamientoPorId(Integer id);

    /**
     * Retorna un entrenamiento por su nombre
     *
     * @param nombre
     * @return objeto Entrenamiento
     */
    List<Entrenamiento> obtenerEntrenamientosPorNombre(String nombre);

    //POST

    /**
     * Crea un entrenamiento
     *
     * @param entrenamiento
     * @return objeto Entrenamiento
     */
    Entrenamiento crearEntrenamiento(Entrenamiento entrenamiento);

    //PUT

    /**
     * Actualiza un entrenamiento basándonos en su id y los nuevos datos
     *
     * @param entrenamiento
     * @param id
     * @return objeto Entrenamiento
     */
    Entrenamiento actualizarEntrenamiento(Integer id, Entrenamiento entrenamiento);

    /**
     * Actualiza un entrenamiento basándonos en su nombre y los nuevos datos
     *
     * @param entrenamiento
     * @param nombre
     * @return objeto Entrenamiento
     */
    Entrenamiento actualizarEntrenamientoPorNombre(String nombre, Entrenamiento entrenamiento);

    //DELETE

    /**
     * Elimina un ejercicio por su id
     * @param id
     */
    void EliminarEjercicio(Integer id);

    /**
     * Elimina un ejercicio por su id
     * @param nombre
     */
    void EliminarEjercicioPorNombre(String nombre);


}
