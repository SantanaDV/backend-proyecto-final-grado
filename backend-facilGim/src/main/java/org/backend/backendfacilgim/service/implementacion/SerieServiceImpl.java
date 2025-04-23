package org.backend.backendfacilgim.service.implementacion;

import org.backend.backendfacilgim.entity.Serie;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.repository.SerieRepository;
import org.backend.backendfacilgim.service.SerieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerieServiceImpl implements SerieService {

    private final SerieRepository serieRepository;

    public SerieServiceImpl(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    @Override
    public List<Serie> listarPorEntrenamientoEjercicio(Integer entrenamientoEjercicioId) {
        if (entrenamientoEjercicioId == null || entrenamientoEjercicioId <= 0) {
            throw new CustomException("ID de entrenamiento-ejercicio inválido.");
        }
        return serieRepository.findByEntrenamientoEjercicio_Id(entrenamientoEjercicioId);
    }

    @Override
    public Serie crear(Serie serie) {
        if (serie.getEntrenamientoEjercicio() == null || serie.getEntrenamientoEjercicio().getId() == null) {
            throw new CustomException("La serie debe estar asociada a un entrenamiento-ejercicio válido.");
        }
        return serieRepository.save(serie);
    }

    @Override
    public Serie actualizar(Integer id, Serie datosNuevos) {
        Serie existente = obtenerPorId(id);
        existente.setNumeroSerie(datosNuevos.getNumeroSerie());
        existente.setRepeticiones(datosNuevos.getRepeticiones());
        existente.setPeso(datosNuevos.getPeso());
        return serieRepository.save(existente);
    }

    @Override
    public void eliminar(Integer id) {
        Serie existente = obtenerPorId(id);
        serieRepository.delete(existente);
    }

    @Override
    public Serie obtenerPorId(Integer id) {
        return serieRepository.findById(id)
                .orElseThrow(() -> new CustomException("Serie no encontrada con ID: " + id));
    }
}
