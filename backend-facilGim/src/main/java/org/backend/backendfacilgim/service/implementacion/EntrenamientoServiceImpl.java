package org.backend.backendfacilgim.service.implementacion;


import org.backend.backendfacilgim.dto.EntrenamientoDTO;
import org.backend.backendfacilgim.entity.Ejercicio;
import org.backend.backendfacilgim.entity.Entrenamiento;
import org.backend.backendfacilgim.entity.TipoEntrenamiento;
import org.backend.backendfacilgim.entity.Usuario;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.repository.EjercicioRepository;
import org.backend.backendfacilgim.repository.EntrenamientoRepository;
import org.backend.backendfacilgim.repository.TipoEntrenamientoRepository;
import org.backend.backendfacilgim.repository.UsuarioRepository;
import org.backend.backendfacilgim.service.EntrenamientoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntrenamientoServiceImpl implements EntrenamientoService {

    private final EntrenamientoRepository entrenamientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoEntrenamientoRepository tipoEntrenamientoRepository;
    private final EjercicioRepository ejercicioRepository;

    public EntrenamientoServiceImpl(
            EntrenamientoRepository entrenamientoRepository,
            UsuarioRepository usuarioRepository,
            TipoEntrenamientoRepository tipoEntrenamientoRepository,
            EjercicioRepository ejercicioRepository
    ) {
        this.entrenamientoRepository = entrenamientoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoEntrenamientoRepository = tipoEntrenamientoRepository;
        this.ejercicioRepository = ejercicioRepository;
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
    public Entrenamiento crearEntrenamientoDesdeDTO(EntrenamientoDTO dto) {
      Entrenamiento entrenamiento = new Entrenamiento();
      rellenarEntrenamientoDesdeDTO(entrenamiento,dto);
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
    public Entrenamiento actualizarEntrenamientoDesdeDTO(Integer id, EntrenamientoDTO dto) {
        Entrenamiento existente = entrenamientoRepository.findById(id)
                .orElseThrow(() -> new CustomException("Entrenamiento no encontrado con ID: " + id));

        rellenarEntrenamientoDesdeDTO(existente,dto);
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

    /**
     * Método auxiliar para asignar a una instancia de Entrenamiento los valores provenientes de un DTO.
     * Este método se encarga de:
     *  - Recuperar el Usuario y el TipoEntrenamiento a partir de sus IDs en el DTO.
     *  - Recuperar la lista de Ejercicios a partir de sus IDs.
     *  - Asignar las propiedades básicas (nombre, descripción, fecha y duración).
     *  - Enlazar los Ejercicios con el Entrenamiento (actualizando la relación bidireccional).
     *
     * @param entrenamiento Instancia de Entrenamiento a la que se asignarán los valores.
     * @param dto           Data Transfer Object que contiene la información a asignar.
     */
    private void rellenarEntrenamientoDesdeDTO(Entrenamiento entrenamiento, EntrenamientoDTO dto) {
        // Validamos y obtenemos el Usuario
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new CustomException("Usuario no encontrado con ID: " + dto.getUsuarioId()));

        // Validamos y obtenemos el Tipo de Entrenamiento
        TipoEntrenamiento tipo = tipoEntrenamientoRepository.findById(dto.getTipoEntrenamientoId())
                .orElseThrow(() -> new CustomException("Tipo de entrenamiento no encontrado con ID: " + dto.getTipoEntrenamientoId()));

        // Recuperamos y validamos cada Ejercicio a partir de sus IDs
        List<Ejercicio> ejercicios = dto.getEjerciciosId().stream()
                .map(ejercicioId -> ejercicioRepository.findById(ejercicioId)
                        .orElseThrow(() -> new CustomException("Ejercicio no encontrado con ID: " + ejercicioId)))
                .collect(Collectors.toList());

        // Asignamos los valores básicos del DTO al entrenamiento
        entrenamiento.setNombre(dto.getNombre());
        entrenamiento.setDescripcion(dto.getDescripcion());
        entrenamiento.setFechaEntrenamiento(dto.getFechaEntrenamiento());
        entrenamiento.setDuracion(dto.getDuracion());
        entrenamiento.setUsuario(usuario);
        entrenamiento.setTipoEntrenamiento(tipo);

        // Limpiamos los ejercicios actuales (en caso de actualización)
        if (entrenamiento.getEjercicios() == null) {
            entrenamiento.setEjercicios(new ArrayList<>());
        } else {
            entrenamiento.getEjercicios().clear();
        }

        // Establecemos la relación bidireccional entre cada ejercicio y el entrenamiento
        for (Ejercicio ejercicio : ejercicios) {
            ejercicio.setEntrenamiento(entrenamiento);
            entrenamiento.getEjercicios().add(ejercicio);
        }
    }
}
