package org.backend.backendfacilgim.service.implementacion;

import org.backend.backendfacilgim.entity.Ejercicio;
import org.backend.backendfacilgim.repository.EjercicioRepository;
import org.backend.backendfacilgim.service.EjercicioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EjercicioServiceImpl implements EjercicioService {
    private final EjercicioRepository ejercicioRepository;

    public EjercicioServiceImpl(EjercicioRepository ejercicioRepository) {
        this.ejercicioRepository = ejercicioRepository;
    }

    @Override
    public Optional<Ejercicio> getEjercicio(Integer ejercicioId) {
        return ejercicioRepository.findById(ejercicioId);
    }

    @Override
    public Ejercicio getEjercicioByNombre(String nombreEjercicio) {
        return Optional.ofNullable(ejercicioRepository.getEjercicioByNombre(nombreEjercicio))
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado por nombre: " + nombreEjercicio));
    }


    @Override
    public List<Ejercicio> listarEjercicios() {
        return ejercicioRepository.findAll();
    }

    @Override
    public Ejercicio crearEjercicio(Ejercicio ejercicio) {
        return ejercicioRepository.save(ejercicio);
    }

    @Override
    public Ejercicio actualizarEjercicio(Integer ejercicioId, Ejercicio datosNuevos) {

        return actualizarEjercicio(ejercicioRepository.findById(ejercicioId).orElseThrow(() ->new RuntimeException("Ejercicio no encontrado con ID: " + ejercicioId)), datosNuevos);


    }

    @Override
    public Ejercicio actualizarEjercicioPorNombre(String nombreEjercicio, Ejercicio datosNuevos, String username) {
        //Implementar la busqueda por nombre

        return actualizarEjercicio(ejercicioRepository.getEjercicioByNombre(nombreEjercicio), datosNuevos);
    }

    @Override
    public void eliminarEjercicio(Integer ejercicioId) {
        ejercicioRepository.delete(ejercicioRepository.findById(ejercicioId).orElseThrow(() ->new RuntimeException("Ejercicio no encontrado con ID: " + ejercicioId)));
    }

    @Override
    public void eliminarEjercicioPorNombre(String nombre) {
        ejercicioRepository.deleteEjercicioByNombre(nombre);
    }

    private Ejercicio actualizarEjercicio(Ejercicio ejercicioEncontrado, Ejercicio ejercicioDatosNuevos) {
        ejercicioEncontrado.setEntrenamiento(ejercicioDatosNuevos.getEntrenamiento());
        ejercicioEncontrado.setPeso(ejercicioDatosNuevos.getPeso());
        ejercicioEncontrado.setNombre(ejercicioDatosNuevos.getNombre());
        ejercicioEncontrado.setRepeticiones(ejercicioDatosNuevos.getRepeticiones());
        ejercicioEncontrado.setImagenUrl(ejercicioDatosNuevos.getImagenUrl());
        return ejercicioEncontrado;
    }
}
