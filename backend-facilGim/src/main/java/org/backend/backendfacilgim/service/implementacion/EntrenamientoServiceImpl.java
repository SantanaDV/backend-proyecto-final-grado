package org.backend.backendfacilgim.service.implementacion;


import org.backend.backendfacilgim.entity.Entrenamiento;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.repository.EntrenamientoRepository;
import org.backend.backendfacilgim.service.EntrenamientoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EntrenamientoServiceImpl implements EntrenamientoService {

    private final EntrenamientoRepository entrenamientoRepository;

    public EntrenamientoServiceImpl(EntrenamientoRepository entrenamientoRepository) {
        this.entrenamientoRepository = entrenamientoRepository;
    }

    @Override
    public List<Entrenamiento> obtenerTodosLosEntrenamiento() {
        return entrenamientoRepository.findAll();
    }

    @Override
    public List<Entrenamiento> obtenerEntrenamientosEntreDosFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return entrenamientoRepository.findByFechaEntrenamientoBetween(fechaInicio, fechaFin);
    }

    @Override
    public Optional<Entrenamiento> obtenerEntrenamientoPorId(Integer id) {
        return entrenamientoRepository.findById(id);
    }

    @Override
    public List<Entrenamiento> obtenerEntrenamientosPorNombre(String nombre) {
        return entrenamientoRepository.findEntrenamientosByNombre(nombre);
    }

    @Override
    public Entrenamiento crearEntrenamiento(Entrenamiento entrenamiento) {
        return entrenamientoRepository.save(entrenamiento);
    }

    @Override
    public Entrenamiento actualizarEntrenamiento(Integer id, Entrenamiento datosNuevos) {
        Entrenamiento entrenamientoExistente = entrenamientoRepository.findById(id)
                .orElseThrow(() -> new CustomException("Entrenamiento no encontrado con ID: " + id));

        return actualizarEntrenamiento(entrenamientoExistente, datosNuevos);
    }

    @Override
    public Entrenamiento actualizarEntrenamientoPorNombre(String nombre, Entrenamiento datosNuevos) {
        List<Entrenamiento> entrenamientos = entrenamientoRepository.findEntrenamientosByNombre(nombre);
        if (entrenamientos.isEmpty()) {
            throw new CustomException("No se encontró ningún entrenamiento con nombre: " + nombre);
        }
        Entrenamiento entrenamientoExistente = entrenamientos.get(0);
        return actualizarEntrenamiento(entrenamientoExistente, datosNuevos);
    }

    @Override
    public void eliminarEntrenamiento(Integer id) {
        Entrenamiento entrenamiento = entrenamientoRepository.findById(id)
                .orElseThrow(() -> new CustomException("Entrenamiento no encontrado con ID: " + id));
        entrenamientoRepository.delete(entrenamiento);
    }

    @Override
    public void eliminarEntrenamientoPorNombre(String nombre) {
        List<Entrenamiento> entrenamientos = entrenamientoRepository.findEntrenamientosByNombre(nombre);
        if (entrenamientos.isEmpty()) {
            throw new CustomException("No se encontró ningún entrenamiento con nombre: " + nombre);
        }
        entrenamientoRepository.deleteAll(entrenamientos);
    }

    private Entrenamiento actualizarEntrenamiento(Entrenamiento entrenamientoEncontrado, Entrenamiento entrenamientoDatosNuevos) {
        entrenamientoEncontrado.setFechaEntrenamiento(entrenamientoDatosNuevos.getFechaEntrenamiento());
        entrenamientoEncontrado.setTipoEntrenamiento(entrenamientoDatosNuevos.getTipoEntrenamiento());
        entrenamientoEncontrado.setDescripcion(entrenamientoDatosNuevos.getDescripcion());
        entrenamientoEncontrado.setNombre(entrenamientoDatosNuevos.getNombre());
        entrenamientoEncontrado.setDuracion(entrenamientoDatosNuevos.getDuracion());
        entrenamientoEncontrado.setEjercicios(entrenamientoDatosNuevos.getEjercicios());
        entrenamientoEncontrado.setUsuario(entrenamientoDatosNuevos.getUsuario());
        return entrenamientoRepository.save(entrenamientoEncontrado);
    }
}
