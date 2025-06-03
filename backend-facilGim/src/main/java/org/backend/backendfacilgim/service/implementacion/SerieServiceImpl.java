package org.backend.backendfacilgim.service.implementacion;

import org.backend.backendfacilgim.entity.Serie;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.repository.SerieRepository;
import org.backend.backendfacilgim.service.SerieService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del servicio {@link SerieService} que gestiona las operaciones
 * CRUD para la entidad {@link Serie}, asociadas a la tabla de series de un
 * entrenamiento-ejercicio.
 *
 * Autor: Francisco Santana
 */
@Service
public class SerieServiceImpl implements SerieService {

    private final SerieRepository serieRepository;

    /**
     * Constructor que inicializa el repositorio de {@link Serie}.
     *
     * @param serieRepository Repositorio encargado de las operaciones de acceso a datos de Serie.
     */
    public SerieServiceImpl(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    /**
     * Lista todas las series asociadas a un registro específico de entrenamiento-ejercicio.
     *
     * @param entrenamientoEjercicioId ID del registro en la tabla intermedia
     *                                entrenamiento_ejercicio.
     * @return Lista de objetos {@link Serie} asociados al entrenamiento-ejercicio.
     * @throws CustomException Si el ID es nulo o no es un valor positivo.
     */
    @Override
    public List<Serie> listarPorEntrenamientoEjercicio(Integer entrenamientoEjercicioId) {
        if (entrenamientoEjercicioId == null || entrenamientoEjercicioId <= 0) {
            throw new CustomException("ID de entrenamiento-ejercicio inválido.");
        }
        return serieRepository.findByEntrenamientoEjercicio_Id(entrenamientoEjercicioId);
    }

    /**
     * Crea una nueva serie asociada a un entrenamiento-ejercicio.
     *
     * @param serie Objeto {@link Serie} a almacenar. Debe contener referencia a un
     *              entrenamiento-ejercicio válido.
     * @return La {@link Serie} recién guardada en la base de datos.
     * @throws CustomException Si la serie no está asociada a un entrenamiento-ejercicio válido.
     */
    @Override
    public Serie crear(Serie serie) {
        if (serie.getEntrenamientoEjercicio() == null || serie.getEntrenamientoEjercicio().getId() == null) {
            throw new CustomException("La serie debe estar asociada a un entrenamiento-ejercicio válido.");
        }
        return serieRepository.save(serie);
    }

    /**
     * Actualiza los campos de una serie existente: número de serie, repeticiones y peso.
     *
     * @param id         ID de la {@link Serie} a actualizar.
     * @param datosNuevos Objeto {@link Serie} que contiene los nuevos valores para los campos.
     * @return La {@link Serie} actualizada.
     * @throws CustomException Si no se encuentra la serie con el ID proporcionado.
     */
    @Override
    public Serie actualizar(Integer id, Serie datosNuevos) {
        Serie existente = obtenerPorId(id);
        existente.setNumeroSerie(datosNuevos.getNumeroSerie());
        existente.setRepeticiones(datosNuevos.getRepeticiones());
        existente.setPeso(datosNuevos.getPeso());
        return serieRepository.save(existente);
    }

    /**
     * Elimina una serie por su ID.
     *
     * @param id ID de la {@link Serie} a eliminar.
     * @throws CustomException Si no se encuentra la serie con el ID proporcionado.
     */
    @Override
    public void eliminar(Integer id) {
        Serie existente = obtenerPorId(id);
        serieRepository.delete(existente);
    }

    /**
     * Obtiene una serie por su ID.
     *
     * @param id ID de la {@link Serie} a buscar.
     * @return La {@link Serie} encontrada.
     * @throws CustomException Si no se encuentra la serie con el ID proporcionado.
     */
    @Override
    public Serie obtenerPorId(Integer id) {
        return serieRepository.findById(id)
                .orElseThrow(() -> new CustomException("Serie no encontrada con ID: " + id));
    }
}
