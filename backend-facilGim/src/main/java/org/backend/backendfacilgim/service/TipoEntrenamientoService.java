package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.entity.TipoEntrenamiento;

import java.util.List;
import java.util.Optional;

public interface TipoEntrenamientoService {

    /**
     * Lista todos los tipos de entrenamiento existentes en la base de datos.
     *
     * @return Lista completa de tipos de entrenamiento registrados
     */
    List<TipoEntrenamiento> listarTipos();

    /**
     * Crea un nuevo tipo de entrenamiento y lo guarda en la base de datos.
     * Se lanza una excepción si ya existe otro con el mismo nombre.
     *
     * @param tipo Objeto TipoEntrenamiento recibido
     * @return El tipo de entrenamiento creado
     */
    TipoEntrenamiento crearTipo(TipoEntrenamiento tipo);

    /**
     * Actualiza un tipo de entrenamiento identificado por su ID.
     * Se lanza una excepción si no existe.
     *
     * @param id    ID del tipo de entrenamiento a actualizar
     * @param tipo  Objeto con los nuevos datos
     * @return Tipo de entrenamiento actualizado
     */
    TipoEntrenamiento actualizarTipo(Long id, TipoEntrenamiento tipo);

    /**
     * Elimina un tipo de entrenamiento por su ID.
     * Se lanza una excepción si no existe.
     *
     * @param id ID del tipo de entrenamiento
     */
    void eliminarTipo(Long id);

    /**
     * Obtiene un tipo de entrenamiento a partir de su ID.
     * Se lanza una excepción si no existe.
     *
     * @param id ID del tipo de entrenamiento
     * @return TipoEntrenamiento encontrado
     */
    TipoEntrenamiento obtenerPorId(Long id);

    /**
     * Verifica si ya existe un tipo de entrenamiento con un nombre específico.
     *
     * @param nombre Nombre del tipo
     * @return true si existe, false en caso contrario
     */
    boolean existePorNombre(String nombre);

    Optional<String> obtenerTipoPorId(long id);
}
