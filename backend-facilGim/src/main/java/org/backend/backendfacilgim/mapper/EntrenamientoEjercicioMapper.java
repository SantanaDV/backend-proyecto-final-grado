package org.backend.backendfacilgim.mapper;

import lombok.experimental.UtilityClass;
import org.backend.backendfacilgim.dto.EntrenamientoEjercicioDTO;
import org.backend.backendfacilgim.dto.SerieDTO;
import org.backend.backendfacilgim.entity.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper de utilidad para convertir entre DTOs de EntrenamientoEjercicio y sus entidades.
 *
 * @autor Francisco Santana
 */
@UtilityClass
public class EntrenamientoEjercicioMapper {

    /**
     * Construye una entidad EntrenamientoEjercicio a partir de un DTO, una entidad Entrenamiento
     * y una entidad Ejercicio. También crea las entidades Serie asociadas si el DTO las contiene.
     *
     * @param dto            DTO que contiene el id del ejercicio, orden y lista de SerieDTO.
     * @param entrenamiento  Entidad Entrenamiento a la que se asociará la relación.
     * @param ejercicio      Entidad Ejercicio que se asignará al entrenamiento.
     * @return Una nueva entidad EntrenamientoEjercicio con sus series, lista para persistir.
     */
    public static EntrenamientoEjercicio fromDTO(
            EntrenamientoEjercicioDTO dto,
            Entrenamiento entrenamiento,
            Ejercicio ejercicio
    ) {
        EntrenamientoEjercicio ee = new EntrenamientoEjercicio();
        ee.setEntrenamiento(entrenamiento);
        ee.setEjercicio(ejercicio);
        // Comentarios propios: mapeo de series si existen en el DTO
        if (dto.getSeries() != null && !dto.getSeries().isEmpty()) {
            List<Serie> series = dto.getSeries().stream().map(s -> {
                Serie serie = new Serie();
                serie.setNumeroSerie(s.getNumeroSerie());
                serie.setRepeticiones(s.getRepeticiones());
                serie.setPeso(s.getPeso());
                serie.setEntrenamientoEjercicio(ee);
                return serie;
            }).collect(Collectors.toList());

            ee.setSeries(series);
        }

        return ee;
    }
}
