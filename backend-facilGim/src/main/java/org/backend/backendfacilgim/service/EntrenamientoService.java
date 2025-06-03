package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.dto.EntrenamientoDTO;
import org.backend.backendfacilgim.entity.Entrenamiento;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de entidades {@link Entrenamiento}.
 * <p>
 * Define operaciones CRUD y consultas sobre entrenamientos, así como métodos
 * auxiliares para asignación y remoción de ejercicios en entrenamientos existentes.
 * </p>
 *
 * @author Francisco Santana
 */
public interface EntrenamientoService {

    // GET

    /**
     * Lista todos los entrenamientos registrados en la base de datos.
     *
     * @return Lista de {@link Entrenamiento}
     */
    List<Entrenamiento> obtenerTodosLosEntrenamientos();

    /**
     * Retorna una lista de entrenamientos cuya fecha de entrenamiento
     * se encuentra entre las dos fechas indicadas (inclusive).
     *
     * @param fechaInicio Fecha inicial del rango (inclusive)
     * @param fechaFin    Fecha final del rango (inclusive)
     * @return Lista de {@link Entrenamiento} en ese rango de fechas
     */
    List<Entrenamiento> obtenerEntrenamientosEntreDosFechas(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Retorna un entrenamiento por su ID.
     *
     * @param id ID del entrenamiento a buscar
     * @return {@link Optional} con el {@link Entrenamiento} encontrado, o vacío si no existe
     */
    Optional<Entrenamiento> obtenerEntrenamientoPorId(Integer id);

    /**
     * Retorna una lista de entrenamientos que coinciden con el nombre dado.
     *
     * @param nombre Nombre (o parte) del entrenamiento a buscar
     * @return Lista de {@link Entrenamiento} cuyo nombre contiene la cadena especificada
     */
    List<Entrenamiento> obtenerEntrenamientosPorNombre(String nombre);

    // POST

    /**
     * Crea un nuevo entrenamiento y lo guarda en la base de datos.
     *
     * @param entrenamiento Objeto {@link Entrenamiento} a crear
     * @return El {@link Entrenamiento} recién creado, con su ID asignado
     */
    Entrenamiento crearEntrenamiento(Entrenamiento entrenamiento);

    /**
     * Crea un entrenamiento a partir de un DTO recibido desde el frontend.
     * <p>
     * Convierte el {@link EntrenamientoDTO} en una entidad {@link Entrenamiento},
     * la persiste y establece las relaciones necesarias.
     * </p>
     *
     * @param entrenamientoDTO DTO con datos de entrenamiento
     * @return El {@link Entrenamiento} creado
     */
    Entrenamiento crearDesdeDTO(EntrenamientoDTO dto);

    // PUT

    /**
     * Actualiza un entrenamiento existente, basándose en su ID.
     * <p>
     * Sustituye los datos actuales por los valores del objeto proporcionado.
     * </p>
     *
     * @param id             ID del entrenamiento a actualizar
     * @param entrenamiento  Objeto {@link Entrenamiento} con los nuevos datos
     * @return El {@link Entrenamiento} actualizado
     */
    Entrenamiento actualizarEntrenamiento(Integer id, Entrenamiento entrenamiento);

    /**
     * Actualiza un entrenamiento existente, basándose en su nombre.
     * <p>
     * Sustituye los datos actuales por los valores del objeto proporcionado.
     * </p>
     *
     * @param nombre         Nombre del entrenamiento a actualizar
     * @param entrenamiento  Objeto {@link Entrenamiento} con los nuevos datos
     * @return El {@link Entrenamiento} actualizado
     */
    Entrenamiento actualizarEntrenamientoPorNombre(String nombre, Entrenamiento entrenamiento);

    /**
     * Actualiza un entrenamiento a partir de un DTO, usando su ID.
     * <p>
     * Convierte el {@link EntrenamientoDTO} en entidad y persiste los cambios.
     * </p>
     *
     * @param id  ID del entrenamiento a actualizar
     * @param dto DTO con los nuevos datos de entrenamiento
     * @return El {@link Entrenamiento} actualizado
     */
    Entrenamiento actualizarEntrenamientoDesdeDTO(Integer id, EntrenamientoDTO dto);

    // DELETE

    /**
     * Elimina un entrenamiento por su ID.
     *
     * @param id ID del entrenamiento a eliminar
     */
    void eliminarEntrenamiento(Integer id);

    /**
     * Elimina un entrenamiento por su nombre.
     *
     * @param nombre Nombre del entrenamiento a eliminar
     */
    void eliminarEntrenamientoPorNombre(String nombre);

    /**
     * Quita un ejercicio de un entrenamiento existente.
     * <p>
     * Desasocia el ejercicio especificado (por ID) del entrenamiento dado
     * y elimina la relación correspondiente.
     * </p>
     *
     * @param idEntrenamiento ID del entrenamiento del cual quitar el ejercicio
     * @param idEjercicio     ID del ejercicio a remover
     * @param username        Nombre de usuario que solicita la operación (para validaciones)
     */
    void quitarEjercicioDeEntrenamiento(Integer idEntrenamiento, Integer idEjercicio, String username);

    /**
     * Busca todos los entrenamientos asociados a un usuario dado.
     *
     * @param usuarioIdUsuario ID del usuario
     * @return Lista de {@link Entrenamiento} pertenecientes a ese usuario
     */
    List<Entrenamiento> encontrarEntrenamientoPorIdUsuario(Integer usuarioIdUsuario);
}
