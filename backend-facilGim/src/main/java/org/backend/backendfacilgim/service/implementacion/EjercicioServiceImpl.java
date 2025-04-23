package org.backend.backendfacilgim.service.implementacion;

import org.backend.backendfacilgim.dto.EjercicioDTO;
import org.backend.backendfacilgim.entity.Ejercicio;
import org.backend.backendfacilgim.entity.Entrenamiento;
import org.backend.backendfacilgim.entity.EntrenamientoEjercicio;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.mapper.EjercicioMapper;
import org.backend.backendfacilgim.repository.EntrenamientoEjercicioRepository;
import org.backend.backendfacilgim.repository.EntrenamientoRepository;
import org.backend.backendfacilgim.repository.EjercicioRepository;
import org.backend.backendfacilgim.service.EjercicioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EjercicioServiceImpl implements EjercicioService {

    private final EntrenamientoRepository entrenamientoRepository;
    private final EjercicioRepository ejercicioRepository;
    private final EntrenamientoEjercicioRepository entrenamientoEjercicioRepository;

    public EjercicioServiceImpl(
            EntrenamientoRepository entrenamientoRepository,
            EjercicioRepository ejercicioRepository,
            EntrenamientoEjercicioRepository entrenamientoEjercicioRepository
    ) {
        this.entrenamientoRepository = entrenamientoRepository;
        this.ejercicioRepository = ejercicioRepository;
        this.entrenamientoEjercicioRepository = entrenamientoEjercicioRepository;
    }

    @Override
    public Optional<Ejercicio> getEjercicio(Integer ejercicioId) {
        return ejercicioRepository.findById(ejercicioId);
    }

    @Override
    public Optional<Ejercicio> getEjercicioByNombre(String nombreEjercicio) {
        Ejercicio ejercicio = ejercicioRepository.findByNombre(nombreEjercicio);
        if (ejercicio == null) {
            throw new CustomException("Ejercicio no encontrado por nombre: " + nombreEjercicio);
        }
        return Optional.of(ejercicio);
    }

    @Override
    public List<Ejercicio> listarEjercicios() {
        return ejercicioRepository.findAll();
    }

    @Override
    public List<EjercicioDTO> listarEjerciciosPorEntrenamientoYUsuario(Integer idEntrenamiento, String username) {
        Entrenamiento entrenamiento = entrenamientoRepository.findById(idEntrenamiento)
                .orElseThrow(() -> new CustomException("Entrenamiento no encontrado"));

        if (!entrenamiento.getUsuario().getUsername().equals(username)) {
            throw new CustomException("Este entrenamiento no pertenece al usuario");
        }

        List<EntrenamientoEjercicio> relaciones = entrenamientoEjercicioRepository.findByEntrenamiento(entrenamiento);

        return relaciones.stream()
                .map(rel -> EjercicioMapper.toDTO(rel.getEjercicio(), rel))
                .collect(Collectors.toList());
    }

    @Override
    public Ejercicio crearEjercicio(Ejercicio ejercicio) {
        return ejercicioRepository.save(ejercicio);
    }

    @Override
    public Ejercicio actualizarEjercicio(Integer ejercicioId, Ejercicio datosNuevos) {
        Ejercicio ejercicio = ejercicioRepository.findById(ejercicioId)
                .orElseThrow(() -> new CustomException("Ejercicio no encontrado con ID: " + ejercicioId));

        ejercicio.setNombre(datosNuevos.getNombre());
        ejercicio.setPeso(datosNuevos.getPeso());
        ejercicio.setRepeticiones(datosNuevos.getRepeticiones());
        ejercicio.setImagenUrl(datosNuevos.getImagenUrl());

        return ejercicioRepository.save(ejercicio);
    }

    @Override
    public Ejercicio actualizarEjercicioPorNombre(String nombreEjercicio, Ejercicio datosNuevos, String username) {
        Ejercicio ejercicio = ejercicioRepository.findByNombre(nombreEjercicio);
        if (ejercicio == null) {
            throw new CustomException("No se encontró el ejercicio con nombre: " + nombreEjercicio);
        }

        boolean perteneceAlUsuario = entrenamientoEjercicioRepository
                .findByEjercicio_IdEjercicio(ejercicio.getIdEjercicio())
                .stream()
                .anyMatch(rel -> rel.getEntrenamiento().getUsuario().getUsername().equals(username));

        if (!perteneceAlUsuario) {
            throw new CustomException("No tienes permiso para modificar este ejercicio.");
        }

        return actualizarEjercicio(ejercicio.getIdEjercicio(), datosNuevos);
    }

    @Override
    public void eliminarEjercicio(Integer ejercicioId) {
        Ejercicio ejercicio = ejercicioRepository.findById(ejercicioId)
                .orElseThrow(() -> new CustomException("Ejercicio no encontrado con ID: " + ejercicioId));
        ejercicioRepository.delete(ejercicio);
    }

    @Override
    public void eliminarEjercicioPorNombre(String nombreEjercicio, String username) {
        Ejercicio ejercicio = ejercicioRepository.findByNombre(nombreEjercicio);
        if (ejercicio == null) {
            throw new CustomException("No se encontró el ejercicio con nombre: " + nombreEjercicio);
        }

        boolean perteneceAlUsuario = entrenamientoEjercicioRepository
                .findByEjercicio_IdEjercicio(ejercicio.getIdEjercicio())
                .stream()
                .anyMatch(rel -> rel.getEntrenamiento().getUsuario().getUsername().equals(username));

        if (!perteneceAlUsuario) {
            throw new CustomException("No tienes permiso para eliminar este ejercicio.");
        }

        ejercicioRepository.deleteByNombre(nombreEjercicio);
    }
}
