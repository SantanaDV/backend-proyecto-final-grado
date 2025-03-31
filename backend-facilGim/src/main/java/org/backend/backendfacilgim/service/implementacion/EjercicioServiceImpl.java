package org.backend.backendfacilgim.service.implementacion;

import org.backend.backendfacilgim.entity.Ejercicio;
import org.backend.backendfacilgim.exception.CustomException;
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
    public Optional<Ejercicio> getEjercicioByNombre(String nombreEjercicio) {
        Ejercicio ejercicio = ejercicioRepository.getEjercicioByNombre(nombreEjercicio);
        if(ejercicio == null) {
            throw new CustomException("Ejercicio no encontrado por nombre: " + nombreEjercicio);
        }
        return Optional.of(ejercicio);
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

        return ejercicioRepository.save(actualizarEjercicio(ejercicioRepository.findById(ejercicioId).orElseThrow(() ->new CustomException("Ejercicio no encontrado con ID: " + ejercicioId)), datosNuevos));


    }

    @Override
    public Ejercicio actualizarEjercicioPorNombre(String nombreEjercicio, Ejercicio datosNuevos, String username) {
        //Implementar la busqueda por nombre
        Ejercicio ejercicioExistente = ejercicioRepository
                .findNombreAndEntrenamiento_Usuario_Username(nombreEjercicio, username);
        if(ejercicioExistente == null) {
            throw new CustomException("No se encontró el ejercicio " + nombreEjercicio + " para el usuario " + username);
        }
        // Actualizar los campos con los datos nuevos
        return ejercicioRepository.save(actualizarEjercicio(ejercicioExistente, datosNuevos));
    }

    @Override
    public void eliminarEjercicio(Integer ejercicioId) {
        ejercicioRepository.delete(ejercicioRepository.findById(ejercicioId).orElseThrow(() ->new CustomException("Ejercicio no encontrado con ID: " + ejercicioId)));
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
