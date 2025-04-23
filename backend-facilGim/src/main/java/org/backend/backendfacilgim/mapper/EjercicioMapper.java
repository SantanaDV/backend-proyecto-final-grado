package org.backend.backendfacilgim.mapper;

import lombok.experimental.UtilityClass;
import org.backend.backendfacilgim.dto.EjercicioDTO;
import org.backend.backendfacilgim.dto.SerieDTO;
import org.backend.backendfacilgim.entity.EntrenamientoEjercicio;
import org.backend.backendfacilgim.entity.Ejercicio;
import org.backend.backendfacilgim.entity.Serie;

import java.util.List;
import java.util.stream.Collectors;
@UtilityClass
public class EjercicioMapper {

    public static EjercicioDTO toDTO(Ejercicio ejercicio, EntrenamientoEjercicio entrenamientoEjercicio) {
        List<SerieDTO> seriesDTO = entrenamientoEjercicio.getSeries()
                .stream()
                .map(SerieMapper::toDTO)
                .collect(Collectors.toList());

        return new EjercicioDTO(
                ejercicio.getIdEjercicio(),
                ejercicio.getNombre(),
                ejercicio.getRepeticiones(),
                ejercicio.getPeso(),
                ejercicio.getImagenUrl(),
                seriesDTO
        );
    }
}
