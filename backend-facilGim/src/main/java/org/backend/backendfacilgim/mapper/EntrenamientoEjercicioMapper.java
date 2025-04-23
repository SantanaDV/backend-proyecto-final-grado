package org.backend.backendfacilgim.mapper;

import lombok.experimental.UtilityClass;
import org.backend.backendfacilgim.dto.EntrenamientoEjercicioDTO;
import org.backend.backendfacilgim.dto.SerieDTO;
import org.backend.backendfacilgim.entity.*;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class EntrenamientoEjercicioMapper {

    public static EntrenamientoEjercicio fromDTO(
            EntrenamientoEjercicioDTO dto,
            Entrenamiento entrenamiento,
            Ejercicio ejercicio
    ) {
        EntrenamientoEjercicio ee = new EntrenamientoEjercicio();
        ee.setEntrenamiento(entrenamiento);
        ee.setEjercicio(ejercicio);

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
