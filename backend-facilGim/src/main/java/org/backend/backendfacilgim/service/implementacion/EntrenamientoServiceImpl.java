// src/main/java/org/backend/backendfacilgim/service/implementacion/EntrenamientoServiceImpl.java
package org.backend.backendfacilgim.service.implementacion;

import org.backend.backendfacilgim.dto.EntrenamientoDTO;
import org.backend.backendfacilgim.entity.*;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.repository.*;
import org.backend.backendfacilgim.service.EntrenamientoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EntrenamientoServiceImpl implements EntrenamientoService {

    private final EntrenamientoRepository entrenamientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoEntrenamientoRepository tipoEntrenamientoRepository;
    private final EjercicioRepository ejercicioRepository;
    private final EntrenamientoEjercicioRepository entrenamientoEjercicioRepository;

    public EntrenamientoServiceImpl(
            EntrenamientoRepository entrenamientoRepository,
            UsuarioRepository usuarioRepository,
            TipoEntrenamientoRepository tipoEntrenamientoRepository,
            EjercicioRepository ejercicioRepository,
            EntrenamientoEjercicioRepository entrenamientoEjercicioRepository
    ) {
        this.entrenamientoRepository = entrenamientoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoEntrenamientoRepository = tipoEntrenamientoRepository;
        this.ejercicioRepository = ejercicioRepository;
        this.entrenamientoEjercicioRepository = entrenamientoEjercicioRepository;
    }

    @Override
    public List<Entrenamiento> obtenerTodosLosEntrenamientos() {
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
    public Entrenamiento crearEntrenamientoDesdeDTO(EntrenamientoDTO dto) {
        Entrenamiento entrenamiento = new Entrenamiento();
        rellenarEntrenamientoDesdeDTO(entrenamiento, dto);
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
        return actualizarEntrenamiento(entrenamientos.get(0), datosNuevos);
    }

    @Override
    public Entrenamiento actualizarEntrenamientoDesdeDTO(Integer id, EntrenamientoDTO dto) {
        Entrenamiento existente = entrenamientoRepository.findById(id)
                .orElseThrow(() -> new CustomException("Entrenamiento no encontrado con ID: " + id));
        rellenarEntrenamientoDesdeDTO(existente, dto);
        return entrenamientoRepository.save(existente);
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

    @Override
    public void quitarEjercicioDeEntrenamiento(Integer idEntrenamiento, Integer idEjercicio, String username) {
        Entrenamiento entrenamiento = entrenamientoRepository.findById(idEntrenamiento)
                .orElseThrow(() -> new CustomException("Entrenamiento no encontrado con ID: " + idEntrenamiento));

        if (!entrenamiento.getUsuario().getUsername().equals(username)) {
            throw new CustomException("El entrenamiento no pertenece al usuario: " + username);
        }

        EntrenamientoEjercicio rel = entrenamientoEjercicioRepository
                .findByEntrenamiento_IdEntrenamientoAndEjercicio_IdEjercicio(idEntrenamiento, idEjercicio)
                .orElseThrow(() -> new CustomException("Relación entrenamiento-ejercicio no encontrada"));

        entrenamientoEjercicioRepository.delete(rel);
    }

    private Entrenamiento actualizarEntrenamiento(Entrenamiento entrenamientoEncontrado, Entrenamiento entrenamientoDatosNuevos) {
        entrenamientoEncontrado.setFechaEntrenamiento(entrenamientoDatosNuevos.getFechaEntrenamiento());
        entrenamientoEncontrado.setTipoEntrenamiento(entrenamientoDatosNuevos.getTipoEntrenamiento());
        entrenamientoEncontrado.setDescripcion(entrenamientoDatosNuevos.getDescripcion());
        entrenamientoEncontrado.setNombre(entrenamientoDatosNuevos.getNombre());
        entrenamientoEncontrado.setDuracion(entrenamientoDatosNuevos.getDuracion());
        entrenamientoEncontrado.setUsuario(entrenamientoDatosNuevos.getUsuario());
        return entrenamientoRepository.save(entrenamientoEncontrado);
    }

    private void rellenarEntrenamientoDesdeDTO(Entrenamiento entrenamiento, EntrenamientoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new CustomException("Usuario no encontrado con ID: " + dto.getUsuarioId()));

        TipoEntrenamiento tipo = tipoEntrenamientoRepository.findById(dto.getTipoEntrenamientoId())
                .orElseThrow(() -> new CustomException("Tipo de entrenamiento no encontrado con ID: " + dto.getTipoEntrenamientoId()));

        List<EntrenamientoEjercicio> relaciones = dto.getEjerciciosId().stream().map(id -> {
            Ejercicio ejercicio = ejercicioRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Ejercicio no encontrado con ID: " + id));
            EntrenamientoEjercicio ee = new EntrenamientoEjercicio();
            ee.setEntrenamiento(entrenamiento);
            ee.setEjercicio(ejercicio);
            return ee;
        }).collect(Collectors.toList());

        entrenamiento.setNombre(dto.getNombre());
        entrenamiento.setDescripcion(dto.getDescripcion());
        entrenamiento.setFechaEntrenamiento(dto.getFechaEntrenamiento());
        entrenamiento.setDuracion(dto.getDuracion());
        entrenamiento.setUsuario(usuario);
        entrenamiento.setTipoEntrenamiento(tipo);
        entrenamiento.setEntrenamientoEjercicios(new HashSet<>(relaciones));
    }
}
