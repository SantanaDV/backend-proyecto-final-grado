package org.backend.backendfacilgim.service.implementacion;

import org.backend.backendfacilgim.dto.EjercicioDTO;
import org.backend.backendfacilgim.dto.SerieDTO;
import org.backend.backendfacilgim.entity.Ejercicio;
import org.backend.backendfacilgim.entity.Entrenamiento;
import org.backend.backendfacilgim.entity.EntrenamientoEjercicio;
import org.backend.backendfacilgim.entity.Serie;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.mapper.EjercicioMapper;
import org.backend.backendfacilgim.repository.EntrenamientoEjercicioRepository;
import org.backend.backendfacilgim.repository.EntrenamientoRepository;
import org.backend.backendfacilgim.repository.EjercicioRepository;
import org.backend.backendfacilgim.service.EjercicioService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación de {@link EjercicioService} que gestiona tanto el catálogo de ejercicios
 * como sus instancias dentro de entrenamientos (relaciones con series y orden).
 *
 * Autor: Francisco Santana
 */
@Service
public class EjercicioServiceImpl implements EjercicioService {

    private final EntrenamientoRepository entrenamientoRepo;
    private final EjercicioRepository ejercicioRepo;
    private final EntrenamientoEjercicioRepository relRepo;

    /**
     * Constructor que inyecta los repositorios necesarios.
     *
     * @param entrenamientoRepo Repositorio de {@link Entrenamiento}.
     * @param ejercicioRepo     Repositorio de {@link Ejercicio}.
     * @param relRepo           Repositorio de {@link EntrenamientoEjercicio}.
     */
    public EjercicioServiceImpl(
            EntrenamientoRepository entrenamientoRepo,
            EjercicioRepository ejercicioRepo,
            EntrenamientoEjercicioRepository relRepo
    ) {
        this.entrenamientoRepo = entrenamientoRepo;
        this.ejercicioRepo = ejercicioRepo;
        this.relRepo = relRepo;
    }

    // --- Catálogo: CRUD puro sobre Ejercicio ---

    /**
     * Devuelve todos los ejercicios del catálogo.
     *
     * @return Lista de {@link Ejercicio}.
     */
    @Override
    public List<Ejercicio> listarEjercicios() {
        return ejercicioRepo.findAll();
    }

    /**
     * Busca un ejercicio en el catálogo por su ID.
     *
     * @param ejercicioId ID del ejercicio a buscar.
     * @return Un {@link Optional} que contiene el {@link Ejercicio} si existe, o vacío en caso contrario.
     */
    @Override
    public Optional<Ejercicio> getEjercicio(Integer ejercicioId) {
        return ejercicioRepo.findById(ejercicioId);
    }

    /**
     * Crea un nuevo ejercicio en el catálogo.
     * <p>
     * Sólo persiste el nombre y la URL de la imagen.
     *
     * @param dto Objeto {@link Ejercicio} recibido (puede provenir del cliente).
     * @return Ejercicio persistido en la base de datos.
     */
    @Override
    public Ejercicio crearEjercicio(Ejercicio dto) {
        Ejercicio e = new Ejercicio();
        e.setNombre(dto.getNombre());
        e.setImagenUrl(dto.getImagenUrl());
        return ejercicioRepo.save(e);
    }

    /**
     * Actualiza un ejercicio existente en el catálogo.
     * <p>
     * Busca primero por ID; si no lo encuentra, lanza {@link CustomException}.
     * Actualiza el nombre y, si se proporciona, la URL de la imagen.
     *
     * @param id    ID del ejercicio a actualizar.
     * @param datos Objeto con los nuevos datos del ejercicio.
     * @return Ejercicio actualizado y persistido.
     * @throws CustomException si no existe ningún ejercicio con el ID dado.
     */
    @Override
    public Ejercicio actualizarEjercicio(Integer id, Ejercicio datos) {
        Ejercicio e = ejercicioRepo.findById(id)
                .orElseThrow(() -> new CustomException("Ejercicio no encontrado: " + id));
        e.setNombre(datos.getNombre());
        if (datos.getImagenUrl() != null) {
            e.setImagenUrl(datos.getImagenUrl());
        }
        return ejercicioRepo.save(e);
    }

    /**
     * Elimina un ejercicio del catálogo por su ID.
     * <p>
     * Si no existe, lanza {@link CustomException}.
     *
     * @param id ID del ejercicio a eliminar.
     * @throws CustomException si no se encuentra el ejercicio con ese ID.
     */
    @Override
    public void eliminarEjercicio(Integer id) {
        Ejercicio e = ejercicioRepo.findById(id)
                .orElseThrow(() -> new CustomException("Ejercicio no encontrado: " + id));
        ejercicioRepo.delete(e);
    }

    /**
     * Elimina un ejercicio del catálogo por su nombre, validando que el usuario
     * que solicita la eliminación sea propietario dentro de algún entrenamiento.
     * <p>
     * Lanza {@link CustomException} si:
     * - El ejercicio no existe por nombre.
     * - El usuario no tiene permiso (no es propietario de ningún entrenamiento que lo contenga).
     *
     * @param nombreEjercicio Nombre del ejercicio a eliminar.
     * @param username        Nombre de usuario que solicita la eliminación.
     * @throws CustomException si no se encuentra el ejercicio o falta permiso.
     */
    @Override
    public void eliminarEjercicioPorNombre(String nombreEjercicio, String username) {
        Ejercicio e = ejercicioRepo.findByNombre(nombreEjercicio);
        if (e == null) {
            throw new CustomException("Ejercicio no encontrado por nombre: " + nombreEjercicio);
        }
        boolean pertenece = relRepo.findByEjercicio_IdEjercicio(e.getIdEjercicio())
                .stream()
                .anyMatch(rel -> rel.getEntrenamiento().getUsuario().getUsername().equals(username));
        if (!pertenece) {
            throw new CustomException("No tienes permiso para eliminar este ejercicio.");
        }
        ejercicioRepo.delete(e);
    }

    /**
     * Asigna un ejercicio del catálogo a un entrenamiento, creando la relación
     * y las series asociadas con su orden correspondiente.
     * <p>
     * Construye manualmente las entidades de {@link EntrenamientoEjercicio} y {@link Serie},
     * guarda la relación en la base de datos y devuelve un DTO con la información resultante.
     *
     * @param idEntrenamiento ID del entrenamiento al que se asigna el ejercicio.
     * @param idEjercicio     ID del ejercicio del catálogo a asignar.
     * @param seriesDTO       Lista de DTOs {@link SerieDTO} para crear las series.
     * @param orden           Orden de aparición dentro del entrenamiento (puede ser null).
     * @return {@link EjercicioDTO} que incluye datos del ejercicio y sus series.
     * @throws CustomException si el entrenamiento o el ejercicio no existen.
     */
    public EjercicioDTO asignarEjercicioConSeriesAEntrenamiento(
            Integer idEntrenamiento,
            Integer idEjercicio,
            List<SerieDTO> seriesDTO,
            Integer orden
    ) {
        Entrenamiento t = entrenamientoRepo.findById(idEntrenamiento)
                .orElseThrow(() -> new CustomException("Entrenamiento no encontrado: " + idEntrenamiento));
        Ejercicio ej = ejercicioRepo.findById(idEjercicio)
                .orElseThrow(() -> new CustomException("Ejercicio no encontrado: " + idEjercicio));

        // Crear la entidad relacional
        EntrenamientoEjercicio rel = new EntrenamientoEjercicio();
        rel.setEntrenamiento(t);
        rel.setEjercicio(ej);
        rel.setOrden(orden);

        // Construir manualmente la lista de series
        List<Serie> series = new ArrayList<>();
        int numeroSerie = 1;

        for (SerieDTO dto : seriesDTO) {
            Serie s = new Serie();
            s.setPeso(dto.getPeso());
            s.setRepeticiones(dto.getRepeticiones());
            s.setNumeroSerie(dto.getNumeroSerie() != null ? dto.getNumeroSerie() : numeroSerie++);
            s.setEntrenamientoEjercicio(rel); // rel ya existe completamente
            series.add(s);
        }

        rel.setSeries(series);

        // Guardar y retornar DTO
        rel = relRepo.save(rel);
        return EjercicioMapper.toDTO(ej, rel);
    }

    /**
     * Elimina una instancia de ejercicio dentro de un entrenamiento (relación),
     * identificada por el ID de la relación.
     *
     * @param relId ID de la relación entrenamiento-ejercicio a eliminar.
     * @throws CustomException si no se encuentra ninguna relación con ese ID.
     */
    @Override
    public void eliminarInstancia(Integer relId) {
        EntrenamientoEjercicio rel = relRepo.findById(relId)
                .orElseThrow(() -> new CustomException("Instancia no encontrada: " + relId));
        relRepo.delete(rel);
    }

    /**
     * Lista los ejercicios (instancias) asignados a un entrenamiento para un usuario dado.
     * <p>
     * Verifica que el usuario coincida con el propietario del entrenamiento; si no, lanza {@link CustomException}.
     *
     * @param idEntrenamiento ID del entrenamiento a consultar.
     * @param username        Nombre de usuario del propietario.
     * @return Lista de {@link EjercicioDTO} que incluye cada ejercicio y sus series.
     * @throws CustomException si el entrenamiento no existe o el usuario no tiene permiso.
     */
    @Override
    public List<EjercicioDTO> listarEjerciciosPorEntrenamientoYUsuario(
            Integer idEntrenamiento,
            String username
    ) {
        Entrenamiento t = entrenamientoRepo.findById(idEntrenamiento)
                .orElseThrow(() -> new CustomException("Entrenamiento no encontrado: " + idEntrenamiento));
        if (!t.getUsuario().getUsername().equals(username)) {
            throw new CustomException("No tienes permiso sobre este entrenamiento");
        }
        return relRepo.findByEntrenamiento(t).stream()
                .map(rel -> EjercicioMapper.toDTO(rel.getEjercicio(), rel))
                .collect(Collectors.toList());
    }

    /**
     * Actualiza las series y el orden de una instancia (relación) ya existente entre un
     * ejercicio y un entrenamiento.
     * <p>
     * Reinicia las series actuales, asigna las nuevas y, si se proporciona, actualiza el orden.
     *
     * @param relId        ID de la relación entrenamiento-ejercicio a actualizar.
     * @param seriesDTOs   Lista de DTOs {@link SerieDTO} con los datos de las nuevas series.
     * @param orden        Nuevo orden dentro del entrenamiento (puede ser null para mantener el actual).
     * @return {@link EjercicioDTO} actualizado con las nuevas series.
     * @throws CustomException si no se encuentra la relación con ese ID.
     */
    @Override
    public EjercicioDTO actualizarInstanciaConSeries(
            Integer relId,
            List<SerieDTO> seriesDTOs,
            Integer orden
    ) {
        EntrenamientoEjercicio rel = relRepo.findById(relId)
                .orElseThrow(() -> new CustomException("Instancia no encontrada: " + relId));

        // Limpiar las series actuales
        rel.getSeries().clear();

        List<Serie> nuevasSeries = new ArrayList<>();
        int index = 1;

        for (SerieDTO serieDTO : seriesDTOs) {
            Serie s = new Serie();
            s.setNumeroSerie(
                    serieDTO.getNumeroSerie() != null ? serieDTO.getNumeroSerie() : index++
            );
            s.setPeso(serieDTO.getPeso());
            s.setRepeticiones(serieDTO.getRepeticiones());
            s.setEntrenamientoEjercicio(rel); // Ahora sí, sin error
            nuevasSeries.add(s);
        }

        rel.getSeries().addAll(nuevasSeries);

        if (orden != null) {
            rel.setOrden(orden);
        }

        // Guardamos la relación actualizada con las nuevas series
        rel = relRepo.save(rel);

        return EjercicioMapper.toDTO(rel.getEjercicio(), rel);
    }
}
