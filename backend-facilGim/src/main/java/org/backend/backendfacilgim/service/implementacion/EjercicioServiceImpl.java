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
        return ejercicioRepository.getEjercicioByNombre(nombreEjercicio);
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
        return actualizarEjercicio(ejercicioRepository.findEjercicioByIdEjercicio(ejercicioId), datosNuevos);
    }

    @Override
    public Ejercicio actualizarEjercicioPorNombre(String nombreEjercicio, Ejercicio datosNuevos) {

        return actualizarEjercicio(ejercicioRepository.findEjercicioByNombre(nombreEjercicio), datosNuevos);
    }

    @Override
    public void eliminarEjercicio(Integer id) {
        ejercicioRepository.delete(ejercicioRepository.findEjercicioByIdEjercicio(id));
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
        return ejercicioEncontrado;
    }
}
