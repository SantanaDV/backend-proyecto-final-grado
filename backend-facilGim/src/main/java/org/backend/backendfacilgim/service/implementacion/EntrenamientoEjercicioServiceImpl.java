package org.backend.backendfacilgim.service.implementacion;

import org.backend.backendfacilgim.entity.EntrenamientoEjercicio;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.repository.EntrenamientoEjercicioRepository;
import org.backend.backendfacilgim.service.EntrenamientoEjercicioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntrenamientoEjercicioServiceImpl implements EntrenamientoEjercicioService {

    private final EntrenamientoEjercicioRepository entrenamientoEjercicioRepository;

    public EntrenamientoEjercicioServiceImpl(EntrenamientoEjercicioRepository entrenamientoEjercicioRepository) {
        this.entrenamientoEjercicioRepository = entrenamientoEjercicioRepository;
    }

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

    @Override
    public List<EntrenamientoEjercicio> obtenerTodos() {
        return entrenamientoEjercicioRepository.findAll();
    }

    @Override
    public List<EntrenamientoEjercicio> obtenerPorIdEntrenamiento(Integer idEntrenamiento) {
        return entrenamientoEjercicioRepository.findByEntrenamiento_IdEntrenamientoOrderByOrdenAsc(idEntrenamiento);
    }

    @Override
    public EntrenamientoEjercicio obtenerPorId(Integer id) {
        return entrenamientoEjercicioRepository.findById(id)
                .orElseThrow(() -> new CustomException("EntrenamientoEjercicio no encontrado con ID: " + id));
    }

    @Override
    public void eliminarPorId(Integer id) {
        if (!entrenamientoEjercicioRepository.existsById(id)) {
            throw new CustomException("No se puede eliminar: no existe relación con ID: " + id);
        }
        entrenamientoEjercicioRepository.deleteById(id);
    }
}
