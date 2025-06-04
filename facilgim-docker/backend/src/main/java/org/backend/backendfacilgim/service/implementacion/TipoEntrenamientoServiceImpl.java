package org.backend.backendfacilgim.service.implementacion;

import org.backend.backendfacilgim.entity.TipoEntrenamiento;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.repository.TipoEntrenamientoRepository;
import org.backend.backendfacilgim.service.TipoEntrenamientoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de {@link TipoEntrenamientoService} que gestiona la lógica
 * de negocio para la entidad {@link TipoEntrenamiento}. Realiza operaciones
 * CRUD asegurando la integridad de los datos (por ejemplo, nombres únicos).
 *
 * Autor: Francisco Santana
 */
@Service
public class TipoEntrenamientoServiceImpl implements TipoEntrenamientoService {

    private final TipoEntrenamientoRepository repository;

    public TipoEntrenamientoServiceImpl(TipoEntrenamientoRepository repository) {
        this.repository = repository;
    }

    /**
     * Lista todos los tipos de entrenamiento registrados en la base de datos.
     *
     * @return Lista completa de {@link TipoEntrenamiento}.
     */
    @Override
    public List<TipoEntrenamiento> listarTipos() {
        return repository.findAll();
    }

    /**
     * Crea un nuevo tipo de entrenamiento. Valida que el nombre no esté vacío
     * y que no exista ya otro tipo con el mismo nombre.
     *
     * @param tipo Objeto {@link TipoEntrenamiento} a crear.
     * @return El {@link TipoEntrenamiento} recién guardado.
     * @throws CustomException si el nombre está vacío o ya existe un tipo con ese nombre.
     */
    @Override
    public TipoEntrenamiento crearTipo(TipoEntrenamiento tipo) {
        if (tipo.getNombre() == null || tipo.getNombre().isBlank()) {
            throw new CustomException("El nombre del tipo de entrenamiento es obligatorio");
        }

        if (repository.existsByNombre(tipo.getNombre())) {
            throw new CustomException("Ya existe un tipo de entrenamiento con ese nombre");
        }

        return repository.save(tipo);
    }

    /**
     * Actualiza el nombre de un tipo de entrenamiento existente.
     * Valida que el nuevo nombre no esté vacío y que, si cambia,
     * no colisione con otro registro.
     *
     * @param id   ID del {@link TipoEntrenamiento} a actualizar.
     * @param tipo Objeto {@link TipoEntrenamiento} con el nuevo nombre.
     * @return El {@link TipoEntrenamiento} actualizado.
     * @throws CustomException si el nombre está vacío, si no se encuentra el ID
     *                         o si ya existe otro tipo con el mismo nombre.
     */
    @Override
    public TipoEntrenamiento actualizarTipo(Long id, TipoEntrenamiento tipo) {
        if (tipo.getNombre() == null || tipo.getNombre().isBlank()) {
            throw new CustomException("El nombre del tipo de entrenamiento es obligatorio");
        }

        TipoEntrenamiento existente = repository.findById(id)
                .orElseThrow(() -> new CustomException("Tipo de entrenamiento no encontrado con ID: " + id));

        if (!existente.getNombre().equalsIgnoreCase(tipo.getNombre()) &&
                repository.existsByNombre(tipo.getNombre())) {
            throw new CustomException("Ya existe otro tipo de entrenamiento con ese nombre");
        }

        existente.setNombre(tipo.getNombre());
        return repository.save(existente);
    }

    /**
     * Elimina un tipo de entrenamiento por su ID.
     *
     * @param id ID del {@link TipoEntrenamiento} a eliminar.
     * @throws CustomException si no existe un registro con ese ID.
     */
    @Override
    public void eliminarTipo(Long id) {
        if (!repository.existsById(id)) {
            throw new CustomException("No se puede eliminar. Tipo de entrenamiento no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Obtiene un tipo de entrenamiento por su ID.
     *
     * @param id ID del {@link TipoEntrenamiento} a buscar.
     * @return El {@link TipoEntrenamiento} encontrado.
     * @throws CustomException si no existe un registro con ese ID.
     */
    @Override
    public TipoEntrenamiento obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException("Tipo de entrenamiento no encontrado con ID: " + id));
    }

    /**
     * Comprueba si ya existe un tipo de entrenamiento con el nombre indicado.
     *
     * @param nombre Nombre a verificar.
     * @return true si existe, false en caso contrario.
     */
    @Override
    public boolean existePorNombre(String nombre) {
        return repository.existsByNombre(nombre);
    }

    /**
     * Obtiene el nombre de un tipo de entrenamiento dado su ID, envuelto en Optional.
     *
     * @param id ID del {@link TipoEntrenamiento}.
     * @return Optional que contiene el nombre si existe, o vacío si no.
     */
    @Override
    public Optional<String> obtenerTipoPorId(long id) {
        return repository.findById(id).map(TipoEntrenamiento::getNombre);
    }
}
