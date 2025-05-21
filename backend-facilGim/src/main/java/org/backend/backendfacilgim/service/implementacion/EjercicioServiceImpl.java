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

@Service
public class EjercicioServiceImpl implements EjercicioService {

    private final EntrenamientoRepository entrenamientoRepo;
    private final EjercicioRepository ejercicioRepo;
    private final EntrenamientoEjercicioRepository relRepo;

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

    @Override
    public List<Ejercicio> listarEjercicios() {
        return ejercicioRepo.findAll();
    }

    @Override
    public Optional<Ejercicio> getEjercicio(Integer ejercicioId) {
        return ejercicioRepo.findById(ejercicioId);
    }

    @Override
    public Ejercicio crearEjercicio(Ejercicio dto) {
        Ejercicio e = new Ejercicio();
        e.setNombre(dto.getNombre());
        e.setImagenUrl(dto.getImagenUrl());
        return ejercicioRepo.save(e);
    }

    @Override
    public Ejercicio actualizarEjercicio(Integer id, Ejercicio datos) {
        Ejercicio e = ejercicioRepo.findById(id)
                .orElseThrow(() -> new CustomException("Ejercicio no encontrado: " + id));
        e.setNombre(datos.getNombre());
        if(datos.getImagenUrl() != null){
            e.setImagenUrl(datos.getImagenUrl());
        }
        return ejercicioRepo.save(e);
    }

    @Override
    public void eliminarEjercicio(Integer id) {
        Ejercicio e = ejercicioRepo.findById(id)
                .orElseThrow(() -> new CustomException("Ejercicio no encontrado: " + id));
        ejercicioRepo.delete(e);
    }

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







    @Override
    public void eliminarInstancia(Integer relId) {
        EntrenamientoEjercicio rel = relRepo.findById(relId)
                .orElseThrow(() -> new CustomException("Instancia no encontrada: " + relId));
        relRepo.delete(rel);
    }

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
