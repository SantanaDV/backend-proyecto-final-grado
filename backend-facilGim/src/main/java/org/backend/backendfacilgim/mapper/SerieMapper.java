package org.backend.backendfacilgim.mapper;

import lombok.experimental.UtilityClass;
import org.backend.backendfacilgim.dto.SerieDTO;
import org.backend.backendfacilgim.entity.Serie;

@UtilityClass
public class SerieMapper {

    public static SerieDTO toDTO(Serie serie) {
        if (serie == null) return null;

        return new SerieDTO(
                serie.getId(),
                serie.getNumeroSerie(),
                serie.getRepeticiones(),
                serie.getPeso()
        );
    }

    public static Serie toEntity(SerieDTO dto) {
        if (dto == null) return null;

        Serie serie = new Serie();
        serie.setId(dto.getId());
        serie.setNumeroSerie(dto.getNumeroSerie());
        serie.setRepeticiones(dto.getRepeticiones());
        serie.setPeso(dto.getPeso());
        return serie;
    }
}
