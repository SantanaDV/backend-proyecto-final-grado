package org.backend.backendfacilgim.service;

import org.backend.backendfacilgim.entity.TipoEntrenamiento;

import java.util.List;

public interface TipoEntrenamientoService {

    /**
     * Lista todos los tipos de entrenamiento existentes en la base de datos.
     *
     * @return Lista de objetos TipoEntrenamiento
     */
    List<TipoEntrenamiento> listarTipos();

    /**
     * Crea un nuevo tipo de entrenamiento y lo guarda en la base de datos.
     *
     * @param tipo Objeto TipoEntrenamiento recibido en formato JSON
     * @return El tipo de entrenamiento creado
     */
    TipoEntrenamiento crearTipo(TipoEntrenamiento tipo);

    /**
     * Actualiza un tipo de entrenamiento identificado por su ID.
     *
     * @param id Identificador del tipo de entrenamiento a actualizar
     * @param tipo Objeto TipoEntrenamiento con los nuevos datos
     * @return El tipo de entrenamiento actualizado
     */
    TipoEntrenamiento actualizarTipo(Long id, TipoEntrenamiento tipo);

    /**
     * Elimina un tipo de entrenamiento de la base de datos por su ID.
     *
     * @param id Identificador del tipo de entrenamiento a eliminar
     */
    void eliminarTipo(Long id);

    /**
     * Obtiene un tipo de entrenamiento a partir de su ID.
     *
     * @param id Identificador del tipo de entrenamiento
     * @return El tipo de entrenamiento encontrado
     */
    TipoEntrenamiento obtenerPorId(Long id);

}