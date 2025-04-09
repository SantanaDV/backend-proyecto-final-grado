package org.backend.backendfacilgim.service.implementacion;

import org.backend.backendfacilgim.entity.TipoEntrenamiento;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.repository.TipoEntrenamientoRepository;
import org.backend.backendfacilgim.service.TipoEntrenamientoService;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if (repository.existsByNombre(tipo.getNombre())) {
            throw new CustomException("Ya existe un tipo de entrenamiento con ese nombre");
        }
        return repository.save(tipo);
    }

    @Override
    public TipoEntrenamiento actualizarTipo(Long id, TipoEntrenamiento tipo) {
        TipoEntrenamiento existente = repository.findById(id)
                .orElseThrow(() -> new CustomException("Tipo no encontrado"));
        existente.setNombre(tipo.getNombre());
        return repository.save(existente);
    }

    @Override
    public void eliminarTipo(Long id) {
        if (!repository.existsById(id)) {
            throw new CustomException("No se puede eliminar. Tipo no encontrado");
        }
        repository.deleteById(id);
    }

    @Override
    public TipoEntrenamiento obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException("Tipo de entrenamiento no encontrado"));
    }
}