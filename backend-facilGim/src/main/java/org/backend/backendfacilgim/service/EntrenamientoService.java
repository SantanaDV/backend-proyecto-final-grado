package org.backend.backendfacilgim.service;


import org.backend.backendfacilgim.dto.EntrenamientoDTO;
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
    List<Entrenamiento> obtenerTodosLosEntrenamientos();

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

    /**
     * Crea un entrenamiento a partir de un DTO.
     * @param entrenamientoDTO DTO recibido desde frontend
     * @return Entrenamiento creado
     */
    Entrenamiento crearEntrenamientoDesdeDTO(EntrenamientoDTO entrenamientoDTO);


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

    /**
     * Actualiza un entrenamiento desde un DTO por su ID.
     * @param id ID del entrenamiento
     * @param dto DTO con los nuevos datos
     * @return Entrenamiento actualizado
     */
    Entrenamiento actualizarEntrenamientoDesdeDTO(Integer id, EntrenamientoDTO dto);

    //DELETE

    /**
     * Elimina un entrenamiento por su id
     * @param id
     */
    void eliminarEntrenamiento(Integer id);

    /**
     * Elimina un entrenamiento por su nombre
     * @param nombre
     */
    void eliminarEntrenamientoPorNombre(String nombre);

    /***
     * Quitar un ejercicio a un entrenamiento existente por su id y nombre de usuario
     * @param idEntrenamiento
     * @param idEjercicio
     * @param username
     */
    void quitarEjercicioDeEntrenamiento(Integer idEntrenamiento, Integer idEjercicio, String username) ;

}
