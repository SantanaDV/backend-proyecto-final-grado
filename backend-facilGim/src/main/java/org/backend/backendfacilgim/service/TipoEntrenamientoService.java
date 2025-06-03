package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.entity.TipoEntrenamiento;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de tipos de entrenamiento.
 * <p>
 * Define operaciones CRUD y validaciones sobre la entidad {@link TipoEntrenamiento}.
 * </p>
 *
 * @author Francisco Santana
 */
public interface TipoEntrenamientoService {

    /**
     * Lista todos los tipos de entrenamiento existentes en la base de datos.
     *
     * @return Lista completa de {@link TipoEntrenamiento} registrados.
     */
    List<TipoEntrenamiento> listarTipos();

    /**
     * Crea un nuevo tipo de entrenamiento y lo guarda en la base de datos.
     * <p>
     * Lanza una excepción si ya existe otro tipo con el mismo nombre.
     * </p>
     *
     * @param tipo Objeto {@link TipoEntrenamiento} a persistir.
     * @return El {@link TipoEntrenamiento} recién creado, con ID asignado.
     */
    TipoEntrenamiento crearTipo(TipoEntrenamiento tipo);

    /**
     * Actualiza un tipo de entrenamiento identificado por su ID.
     * <p>
     * Lanza una excepción si no existe ningún tipo con el ID proporcionado.
     * </p>
     *
     * @param id   ID del tipo de entrenamiento a actualizar.
     * @param tipo Objeto {@link TipoEntrenamiento} con los nuevos datos.
     * @return El {@link TipoEntrenamiento} actualizado.
     */
    TipoEntrenamiento actualizarTipo(Long id, TipoEntrenamiento tipo);

    /**
     * Elimina un tipo de entrenamiento por su ID.
     * <p>
     * Lanza una excepción si no existe ningún tipo con ese ID.
     * </p>
     *
     * @param id ID del {@link TipoEntrenamiento} a eliminar.
     */
    void eliminarTipo(Long id);

    /**
     * Obtiene un tipo de entrenamiento a partir de su ID.
     * <p>
     * Lanza una excepción si no existe ningún tipo con ese ID.
     * </p>
     *
     * @param id ID del {@link TipoEntrenamiento} buscado.
     * @return El {@link TipoEntrenamiento} encontrado.
     */
    TipoEntrenamiento obtenerPorId(Long id);

    /**
     * Verifica si ya existe un tipo de entrenamiento con un nombre específico.
     *
     * @param nombre Nombre del tipo a comprobar.
     * @return {@code true} si existe, {@code false} en caso contrario.
     */
    boolean existePorNombre(String nombre);

    /**
     * Obtiene el nombre de un tipo de entrenamiento a partir de su ID.
     * <p>
     * Devuelve un {@link Optional} con el nombre si existe, o vacío si no se encuentra.
     * </p>
     *
     * @param id ID del tipo de entrenamiento.
     * @return {@link Optional} con el nombre del tipo, o vacío si no existe.
     */
    Optional<String> obtenerTipoPorId(long id);
}
