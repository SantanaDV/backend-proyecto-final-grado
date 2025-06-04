package org.backend.backendfacilgim.service.implementacion;

import org.backend.backendfacilgim.entity.EntrenamientoEjercicio;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.repository.EntrenamientoEjercicioRepository;
import org.backend.backendfacilgim.service.EntrenamientoEjercicioService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación de {@link EntrenamientoEjercicioService} que gestiona las operaciones
 * CRUD sobre la relación entre entrenamientos y ejercicios.
 *
 * Autor: Francisco Santana
 */
@Service
public class EntrenamientoEjercicioServiceImpl implements EntrenamientoEjercicioService {

    private final EntrenamientoEjercicioRepository entrenamientoEjercicioRepository;

    /**
     * Constructor que inyecta el repositorio necesario para las operaciones.
     *
     * @param entrenamientoEjercicioRepository Repositorio de {@link EntrenamientoEjercicio}.
     */
    public EntrenamientoEjercicioServiceImpl(EntrenamientoEjercicioRepository entrenamientoEjercicioRepository) {
        this.entrenamientoEjercicioRepository = entrenamientoEjercicioRepository;
    }

    /**
     * Crea una nueva relación entre entrenamiento y ejercicio.
     * <p>
     * Antes de guardar, comprueba si ya existe una relación para el mismo entrenamiento
     * y ejercicio; si existe, lanza una {@link CustomException}.
     *
     * @param entity Objeto {@link EntrenamientoEjercicio} a crear.
     * @return La relación creada y persistida en la base de datos.
     * @throws CustomException si ya existe la relación entrenamiento-ejercicio.
     */
    @Override
    public EntrenamientoEjercicio crear(EntrenamientoEjercicio entity) {
        boolean exists = entrenamientoEjercicioRepository
                .findByEntrenamiento_IdEntrenamientoAndEjercicio_IdEjercicio(
                        entity.getEntrenamiento().getIdEntrenamiento(),
                        entity.getEjercicio().getIdEjercicio()
                )
                .isPresent();

        if (exists) {
            throw new CustomException("Ya existe esta relación entrenamiento-ejercicio");
        }

        return entrenamientoEjercicioRepository.save(entity);
    }

    /**
     * Devuelve todas las relaciones entre entrenamientos y ejercicios.
     *
     * @return Lista de todas las entidades {@link EntrenamientoEjercicio}.
     */
    @Override
    public List<EntrenamientoEjercicio> obtenerTodos() {
        return entrenamientoEjercicioRepository.findAll();
    }

    /**
     * Obtiene las relaciones de ejercicios para un entrenamiento específico,
     * ordenadas por el campo {@code orden} de forma ascendente.
     *
     * @param idEntrenamiento ID del entrenamiento cuya relación se desea consultar.
     * @return Lista de {@link EntrenamientoEjercicio} asociados al entrenamiento dado.
     */
    @Override
    public List<EntrenamientoEjercicio> obtenerPorIdEntrenamiento(Integer idEntrenamiento) {
        return entrenamientoEjercicioRepository.findByEntrenamiento_IdEntrenamientoOrderByOrdenAsc(idEntrenamiento);
    }

    /**
     * Busca una relación {@link EntrenamientoEjercicio} por su ID.
     *
     * @param id ID de la relación entrenamiento-ejercicio.
     * @return La entidad encontrada.
     * @throws CustomException si no se encuentra ninguna relación con el ID proporcionado.
     */
    @Override
    public EntrenamientoEjercicio obtenerPorId(Integer id) {
        return entrenamientoEjercicioRepository.findById(id)
                .orElseThrow(() -> new CustomException("EntrenamientoEjercicio no encontrado con ID: " + id));
    }

    /**
     * Elimina una relación entrenamiento-ejercicio por su ID.
     * <p>
     * Comprueba primero si existe una entidad con el ID proporcionado; si no existe,
     * lanza una {@link CustomException}.
     *
     * @param id ID de la relación a eliminar.
     * @throws CustomException si no existe ninguna relación con ese ID.
     */
    @Override
    public void eliminarPorId(Integer id) {
        if (!entrenamientoEjercicioRepository.existsById(id)) {
            throw new CustomException("No se puede eliminar: no existe relación con ID: " + id);
        }
        entrenamientoEjercicioRepository.deleteById(id);
    }
}
