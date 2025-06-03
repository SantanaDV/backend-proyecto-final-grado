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

/**
 * Implementación del servicio {@link EntrenamientoService} que gestiona las operaciones
 * CRUD para la entidad {@link Entrenamiento} y sus relaciones con ejercicios y series.
 *
 * Autor: Francisco Santana
 */
@Service
public class EntrenamientoServiceImpl implements EntrenamientoService {

    private final EntrenamientoRepository entrenamientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoEntrenamientoRepository tipoEntrenamientoRepository;
    private final EjercicioRepository ejercicioRepository;
    private final EntrenamientoEjercicioRepository entrenamientoEjercicioRepository;

    /**
     * Constructor que inicializa todos los repositorios necesarios.
     *
     * @param entrenamientoRepository        Repositorio de entrenamientos.
     * @param usuarioRepository              Repositorio de usuarios.
     * @param tipoEntrenamientoRepository    Repositorio de tipos de entrenamiento.
     * @param ejercicioRepository            Repositorio de ejercicios.
     * @param entrenamientoEjercicioRepository Repositorio de relaciones entrenamiento-ejercicio.
     */
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

    /**
     * Lista todos los entrenamientos almacenados en la base de datos.
     *
     * @return Lista de {@link Entrenamiento}
     */
    @Override
    public List<Entrenamiento> obtenerTodosLosEntrenamientos() {
        return entrenamientoRepository.findAll();
    }

    /**
     * Obtiene los entrenamientos cuya fecha de entrenamiento está entre dos fechas dadas.
     *
     * @param fechaInicio Fecha de inicio del rango (inclusive).
     * @param fechaFin    Fecha de fin del rango (inclusive).
     * @return Lista de {@link Entrenamiento} dentro del rango de fechas.
     */
    @Override
    public List<Entrenamiento> obtenerEntrenamientosEntreDosFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return entrenamientoRepository.findByFechaEntrenamientoBetween(fechaInicio, fechaFin);
    }

    /**
     * Busca un entrenamiento por su ID.
     *
     * @param id ID del entrenamiento.
     * @return {@link Optional} con el entrenamiento si existe, o vacío si no.
     */
    @Override
    public Optional<Entrenamiento> obtenerEntrenamientoPorId(Integer id) {
        return entrenamientoRepository.findById(id);
    }

    /**
     * Busca entrenamientos que coincidan con un nombre dado.
     *
     * @param nombre Nombre o parte del nombre del entrenamiento.
     * @return Lista de entrenamientos con el nombre especificado.
     */
    @Override
    public List<Entrenamiento> obtenerEntrenamientosPorNombre(String nombre) {
        return entrenamientoRepository.findEntrenamientosByNombre(nombre);
    }

    /**
     * Crea un nuevo entrenamiento en la base de datos.
     *
     * @param entrenamiento Objeto {@link Entrenamiento} a crear.
     * @return El {@link Entrenamiento} recién guardado.
     */
    @Override
    public Entrenamiento crearEntrenamiento(Entrenamiento entrenamiento) {
        return entrenamientoRepository.save(entrenamiento);
    }

    /**
     * Actualiza los campos básicos de un entrenamiento existente (no maneja relaciones con ejercicios/series).
     *
     * @param id           ID del entrenamiento a actualizar.
     * @param datosNuevos  Objeto {@link Entrenamiento} con los nuevos datos.
     * @return El {@link Entrenamiento} actualizado.
     * @throws CustomException si no se encuentra el entrenamiento con el ID dado.
     */
    @Override
    public Entrenamiento actualizarEntrenamiento(Integer id, Entrenamiento datosNuevos) {
        Entrenamiento entrenamientoExistente = entrenamientoRepository.findById(id)
                .orElseThrow(() -> new CustomException("Entrenamiento no encontrado con ID: " + id));
        return actualizarEntrenamiento(entrenamientoExistente, datosNuevos);
    }

    /**
     * Actualiza un entrenamiento buscándolo por nombre (el primero que coincida).
     *
     * @param nombre       Nombre del entrenamiento a actualizar.
     * @param datosNuevos  Objeto {@link Entrenamiento} con los nuevos datos.
     * @return El {@link Entrenamiento} actualizado.
     * @throws CustomException si no se encuentra ningún entrenamiento con el nombre dado.
     */
    @Override
    public Entrenamiento actualizarEntrenamientoPorNombre(String nombre, Entrenamiento datosNuevos) {
        List<Entrenamiento> entrenamientos = entrenamientoRepository.findEntrenamientosByNombre(nombre);
        if (entrenamientos.isEmpty()) {
            throw new CustomException("No se encontró ningún entrenamiento con nombre: " + nombre);
        }
        return actualizarEntrenamiento(entrenamientos.get(0), datosNuevos);
    }

    /**
     * Actualiza un entrenamiento a partir de un DTO {@link EntrenamientoDTO}, incluyendo sus relaciones
     * con ejercicios y series. El método es transaccional para asegurar la consistencia de las relaciones.
     *
     * @param id  ID del entrenamiento a actualizar.
     * @param dto Objeto {@link EntrenamientoDTO} con los nuevos datos y relaciones.
     * @return El {@link Entrenamiento} actualizado y guardado en la base de datos.
     * @throws CustomException si no se encuentra el entrenamiento, usuario, tipo o ejercicio especificado.
     */
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

    /**
     * Elimina un entrenamiento por su ID.
     *
     * @param id ID del entrenamiento a eliminar.
     * @throws CustomException si no se encuentra el entrenamiento con el ID dado.
     */
    @Override
    public void eliminarEntrenamiento(Integer id) {
        Entrenamiento entrenamiento = entrenamientoRepository.findById(id)
                .orElseThrow(() -> new CustomException("Entrenamiento no encontrado con ID: " + id));
        entrenamientoRepository.delete(entrenamiento);
    }

    /**
     * Elimina todos los entrenamientos que coincidan con un nombre dado.
     *
     * @param nombre Nombre del entrenamiento a eliminar.
     * @throws CustomException si no se encuentra ningún entrenamiento con el nombre dado.
     */
    @Override
    public void eliminarEntrenamientoPorNombre(String nombre) {
        List<Entrenamiento> entrenamientos = entrenamientoRepository.findEntrenamientosByNombre(nombre);
        if (entrenamientos.isEmpty()) {
            throw new CustomException("No se encontró ningún entrenamiento con nombre: " + nombre);
        }
        entrenamientoRepository.deleteAll(entrenamientos);
    }

    /**
     * Elimina una instancia (relación) de ejercicio en un entrenamiento,
     * verificando que el entrenamiento pertenezca al usuario indicado.
     *
     * @param idEntrenamiento ID del entrenamiento.
     * @param idEjercicio     ID del ejercicio a quitar.
     * @param username        Nombre de usuario propietario del entrenamiento.
     * @throws CustomException si el entrenamiento no existe, no pertenece al usuario,
     *                         o no existe la relación especificada.
     */
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

    /**
     * Crea un nuevo entrenamiento a partir de un DTO {@link EntrenamientoDTO}, incluyendo
     * sus relaciones con ejercicios y series. Este método es transaccional para garantizar
     * la creación conjunta de entrenamiento, relaciones y series.
     *
     * @param dto DTO {@link EntrenamientoDTO} con los datos y relaciones a crear.
     * @return El {@link Entrenamiento} recién creado.
     * @throws CustomException si no se encuentra el usuario, tipo o ejercicio especificado.
     */
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

    /**
     * Obtiene la lista de entrenamientos asociados a un usuario específico.
     *
     * @param usuarioIdUsuario ID del usuario.
     * @return Lista de entrenamientos para el usuario, o lista vacía si no hay ninguno.
     */
    @Override
    public List<Entrenamiento> encontrarEntrenamientoPorIdUsuario(Integer usuarioIdUsuario) {
        if (entrenamientoRepository.findEntrenamientosByUsuario_IdUsuario(usuarioIdUsuario).isEmpty())
            return List.of();
        return entrenamientoRepository.findEntrenamientosByUsuario_IdUsuario(usuarioIdUsuario);
    }

    /**
     * Método auxiliar que actualiza los campos básicos de un {@link Entrenamiento}
     * existente con los datos de otro objeto {@link Entrenamiento}.
     *
     * @param entrenamientoEncontrado Entrenamiento ya persistido.
     * @param entrenamientoDatosNuevos Objeto con nuevos datos.
     * @return El {@link Entrenamiento} actualizado y guardado.
     */
    private Entrenamiento actualizarEntrenamiento(Entrenamiento entrenamientoEncontrado,
                                                  Entrenamiento entrenamientoDatosNuevos) {
        entrenamientoEncontrado.setFechaEntrenamiento(entrenamientoDatosNuevos.getFechaEntrenamiento());
        entrenamientoEncontrado.setTipoEntrenamiento(entrenamientoDatosNuevos.getTipoEntrenamiento());
        entrenamientoEncontrado.setDescripcion(entrenamientoDatosNuevos.getDescripcion());
        entrenamientoEncontrado.setNombre(entrenamientoDatosNuevos.getNombre());
        entrenamientoEncontrado.setDuracion(entrenamientoDatosNuevos.getDuracion());
        entrenamientoEncontrado.setUsuario(entrenamientoDatosNuevos.getUsuario());
        return entrenamientoRepository.save(entrenamientoEncontrado);
    }
}
