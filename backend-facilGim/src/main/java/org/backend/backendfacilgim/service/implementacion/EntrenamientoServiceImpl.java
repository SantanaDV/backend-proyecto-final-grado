// src/main/java/org/backend/backendfacilgim/service/implementacion/EntrenamientoServiceImpl.java
package org.backend.backendfacilgim.service.implementacion;

import org.backend.backendfacilgim.dto.EntrenamientoDTO;
import org.backend.backendfacilgim.dto.EntrenamientoEjercicioDTO;
import org.backend.backendfacilgim.dto.SerieDTO;
import org.backend.backendfacilgim.entity.*;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.repository.*;
import org.backend.backendfacilgim.service.EntrenamientoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

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

    // src/main/java/org/backend/backendfacilgim/service/implementacion/EntrenamientoServiceImpl.java

    @Override
    @Transactional
    public Entrenamiento actualizarEntrenamientoDesdeDTO(Integer id, EntrenamientoDTO dto) {
        Entrenamiento existente = entrenamientoRepository.findById(id)
                .orElseThrow(() -> new CustomException("Entrenamiento no encontrado con ID: " + id));

        // 1) Campos simples
        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        existente.setDuracion(dto.getDuracion());
        existente.setFechaEntrenamiento(dto.getFechaEntrenamiento());

        // 2) Usuario
        Usuario usuario = usuarioRepository.findById(dto.getUsuario().getIdUsuario())
                .orElseThrow(() -> new CustomException("Usuario no encontrado con ID: " + dto.getUsuario().getIdUsuario()));
        existente.setUsuario(usuario);

        // 3) Tipo
        TipoEntrenamiento tipo = tipoEntrenamientoRepository.findById(dto.getTipoEntrenamiento().getId())
                .orElseThrow(() -> new CustomException("Tipo de entrenamiento no encontrado con ID: " + dto.getTipoEntrenamiento().getId()));
        existente.setTipoEntrenamiento(tipo);

        // 4) Eliminar todas las relaciones antiguas usando orphanRemoval
        existente.getEntrenamientoEjercicios().clear();

        // 5) Construir NUEVAS relaciones desde el DTO
        if (dto.getEntrenamientosEjercicios() != null) {
            for (EntrenamientoEjercicioDTO relDTO : dto.getEntrenamientosEjercicios()) {
                // 5.1) Recuperar ejercicio
                Ejercicio ejercicio = ejercicioRepository.findById(relDTO.getEjercicio().getIdEjercicio())
                        .orElseThrow(() -> new CustomException(
                                "Ejercicio no encontrado con ID: " + relDTO.getEjercicio().getIdEjercicio()));

                // 5.2) Crear entidad de relación
                EntrenamientoEjercicio ee = new EntrenamientoEjercicio();
                ee.setEntrenamiento(existente);
                ee.setEjercicio(ejercicio);
                ee.setOrden(relDTO.getOrden());

                // 5.3) Series de esta relación
                List<Serie> listaSeries = new ArrayList<>();
                int contadorSerie = 1;
                if (relDTO.getSeries() != null) {
                    for (SerieDTO sDTO : relDTO.getSeries()) {
                        Serie s = new Serie();
                        // Si DTO trae número de serie, úsalo; sino autoincrementa
                        s.setNumeroSerie(
                                sDTO.getNumeroSerie() != null ? sDTO.getNumeroSerie() : contadorSerie++);
                        s.setRepeticiones(sDTO.getRepeticiones());
                        s.setPeso(sDTO.getPeso());
                        // Enlace bidireccional
                        s.setEntrenamientoEjercicio(ee);
                        listaSeries.add(s);
                    }
                }
                ee.setSeries(listaSeries);

                // 5.4) Añadir al set del entrenamiento (Hibernate insertará estas nuevas)
                existente.getEntrenamientoEjercicios().add(ee);
            }
        }

        // 6) Guardar TODO de una sola vez: Hibernate borrará los viejos (orphanRemoval)
        //    y creará los nuevos
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

    @Override
    @Transactional
    public Entrenamiento crearDesdeDTO(EntrenamientoDTO dto) {
        Entrenamiento entrenamiento = new Entrenamiento();

        entrenamiento.setNombre(dto.getNombre());
        entrenamiento.setDescripcion(dto.getDescripcion());
        entrenamiento.setDuracion(dto.getDuracion());
        entrenamiento.setFechaEntrenamiento(dto.getFechaEntrenamiento());

        Usuario usuario = usuarioRepository.findById(dto.getUsuario().getIdUsuario())
                .orElseThrow(() -> new CustomException("Usuario no encontrado"));
        entrenamiento.setUsuario(usuario);

        TipoEntrenamiento tipo = tipoEntrenamientoRepository.findById(dto.getTipoEntrenamiento().getId())
                .orElseThrow(() -> new CustomException("Tipo de entrenamiento no encontrado"));
        entrenamiento.setTipoEntrenamiento(tipo);

        // Preparamos las relaciones fuera del set original
        Set<EntrenamientoEjercicio> relaciones = new HashSet<>();

        if (dto.getEntrenamientosEjercicios() != null) {
            for (EntrenamientoEjercicioDTO relDTO : dto.getEntrenamientosEjercicios()) {

                Integer idEjercicio = relDTO.getEjercicio().getIdEjercicio();
                Ejercicio ejercicio = ejercicioRepository.findById(idEjercicio)
                        .orElseThrow(() -> new CustomException("Ejercicio no encontrado"));

                EntrenamientoEjercicio ee = new EntrenamientoEjercicio();
                ee.setEntrenamiento(entrenamiento);
                ee.setEjercicio(ejercicio);
                ee.setOrden(relDTO.getOrden());

                List<Serie> series = new ArrayList<>();
                int numeroSerie = 1;

                if (relDTO.getSeries() != null) {
                    for (SerieDTO serieDTO : relDTO.getSeries()) {
                        Serie serie = new Serie();
                        serie.setNumeroSerie(serieDTO.getNumeroSerie() != null ? serieDTO.getNumeroSerie() : numeroSerie++);
                        serie.setRepeticiones(serieDTO.getRepeticiones());
                        serie.setPeso(serieDTO.getPeso());
                        serie.setEntrenamientoEjercicio(ee);
                        series.add(serie);
                    }
                }

                ee.setSeries(series);
                relaciones.add(ee);
            }
        }

        // Solo una vez construidas todas, se asigna el set
        entrenamiento.setEntrenamientoEjercicios(relaciones);

        return entrenamientoRepository.save(entrenamiento);
    }




    @Override
    public List<Entrenamiento> encontrarEntrenamientoPorIdUsuario(Integer usuarioIdUsuario) {

        if(entrenamientoRepository.findEntrenamientosByUsuario_IdUsuario(usuarioIdUsuario).isEmpty())
            return List.of();
        return entrenamientoRepository.findEntrenamientosByUsuario_IdUsuario(usuarioIdUsuario);
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


}
