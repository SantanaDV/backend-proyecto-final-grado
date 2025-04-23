package org.backend.backendfacilgim.service.implementacion;

import org.backend.backendfacilgim.entity.TipoEntrenamiento;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.repository.TipoEntrenamientoRepository;
import org.backend.backendfacilgim.service.TipoEntrenamientoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoEntrenamientoServiceImpl implements TipoEntrenamientoService {

    private final TipoEntrenamientoRepository repository;

    public TipoEntrenamientoServiceImpl(TipoEntrenamientoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TipoEntrenamiento> listarTipos() {
        return repository.findAll();
    }

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

    @Override
    public void eliminarTipo(Long id) {
        if (!repository.existsById(id)) {
            throw new CustomException("No se puede eliminar. Tipo de entrenamiento no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public TipoEntrenamiento obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException("Tipo de entrenamiento no encontrado con ID: " + id));
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return repository.existsByNombre(nombre);
    }

    @Override
    public Optional<String> obtenerTipoPorId(long id) {
        return repository.findById(id).map(TipoEntrenamiento::getNombre);
    }
}
