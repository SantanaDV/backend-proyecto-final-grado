package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.dto.EjercicioDTO;
import org.backend.backendfacilgim.dto.SerieDTO;
import org.backend.backendfacilgim.entity.Ejercicio;
import org.backend.backendfacilgim.exception.CustomException;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para gestionar tanto el catálogo global de ejercicios
 * como las instancias de ejercicios asignadas a entrenamientos.
 * Proporciona operaciones CRUD para ejercicios y métodos específicos para
 * asignar, listar y actualizar ejercicios dentro de un entrenamiento.
 *
 * Autor: Francisco Santana
 */
public interface EjercicioService {

    // —— Catálogo global ——

    /**
     * Recupera todos los ejercicios disponibles en el catálogo general.
     *
     * @return Lista de entidades {@link Ejercicio} representando el catálogo completo.
     */
    List<Ejercicio> listarEjercicios();

    /**
     * Busca un ejercicio en el catálogo por su ID.
     *
     * @param ejercicioId ID del ejercicio a buscar.
     * @return {@link Optional} que contiene el {@link Ejercicio} si existe, o vacío en caso contrario.
     */
    Optional<Ejercicio> getEjercicio(Integer ejercicioId);

    /**
     * Crea un nuevo ejercicio en el catálogo.
     *
     * @param ejercicio Objeto {@link Ejercicio} con los datos a almacenar.
     * @return La entidad {@link Ejercicio} persistida con su ID generado.
     */
    Ejercicio crearEjercicio(Ejercicio ejercicio);

    /**
     * Actualiza un ejercicio existente en el catálogo.
     *
     * @param ejercicioId   ID del ejercicio a actualizar.
     * @param datosNuevos   Objeto {@link Ejercicio} que contiene los nuevos valores.
     * @return El {@link Ejercicio} actualizado.
     */
    Ejercicio actualizarEjercicio(Integer ejercicioId, Ejercicio datosNuevos);

    /**
     * Elimina un ejercicio del catálogo por su ID.
     *
     * @param ejercicioId ID del ejercicio a eliminar.
     */
    void eliminarEjercicio(Integer ejercicioId);

    /**
     * Elimina un ejercicio del catálogo por su nombre, validando
     * que el {@code username} proporcionado sea el propietario.
     *
     * @param nombreEjercicio    Nombre del ejercicio a eliminar.
     * @param username           Nombre de usuario del propietario que solicita la eliminación.
     * @throws CustomException   Si el usuario no tiene permisos o no existe el ejercicio.
     */
    void eliminarEjercicioPorNombre(String nombreEjercicio, String username);

    // —— Instancias en entrenamientos ——

    /**
     * Asigna un ejercicio del catálogo a un entrenamiento específico,
     * junto con una lista de series y su orden en la rutina.
     *
     * @param idEntrenamiento    ID del entrenamiento al que se asigna el ejercicio.
     * @param idEjercicio        ID del ejercicio en el catálogo.
     * @param seriesDTO          Lista de {@link SerieDTO} con repeticiones y peso para cada serie.
     * @param orden              Posición u orden dentro del entrenamiento.
     * @return {@link EjercicioDTO} que representa la instancia creada, incluyendo series y metadatos.
     */
    EjercicioDTO asignarEjercicioConSeriesAEntrenamiento(
            Integer idEntrenamiento,
            Integer idEjercicio,
            List<SerieDTO> seriesDTO,
            Integer orden
    );

    /**
     * Elimina la instancia de un ejercicio ya asignado a un entrenamiento.
     *
     * @param relId     ID de la relación {@code entrenamiento_ejercicio} a eliminar.
     */
    void eliminarInstancia(Integer relId);

    /**
     * Lista todas las instancias de ejercicios (incluyendo series)
     * asociadas a un entrenamiento y usuario dados.
     *
     * @param idEntrenamiento    ID del entrenamiento en el que se buscan instancias.
     * @param username           Nombre de usuario propietario del entrenamiento.
     * @return Lista de {@link EjercicioDTO} con los datos de cada ejercicio y sus series.
     */
    List<EjercicioDTO> listarEjerciciosPorEntrenamientoYUsuario(Integer idEntrenamiento, String username);

    /**
     * Actualiza la información (series y orden) de una instancia
     * de ejercicio asignada a un entrenamiento.
     *
     * @param relId      ID de la relación {@code entrenamiento_ejercicio} a actualizar.
     * @param series     Lista de {@link SerieDTO} con los nuevos valores de repeticiones y peso.
     * @param orden      Nuevo orden dentro del entrenamiento.
     * @return {@link EjercicioDTO} que representa la instancia actualizada.
     */
    EjercicioDTO actualizarInstanciaConSeries(Integer relId, List<SerieDTO> series, Integer orden);
}
