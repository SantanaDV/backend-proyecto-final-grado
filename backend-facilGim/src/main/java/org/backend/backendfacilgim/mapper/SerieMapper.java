package org.backend.backendfacilgim.mapper;

import lombok.experimental.UtilityClass;
import org.backend.backendfacilgim.dto.SerieDTO;
import org.backend.backendfacilgim.entity.Serie;

/**
 * Mapper de utilidad para convertir entre la entidad Serie y su DTO correspondiente.
 *
 * @autor Francisco Santana
 */
@UtilityClass
public class SerieMapper {

    /**
     * Convierte una entidad Serie a su DTO equivalente.
     *
     * @param serie Entidad Serie que se desea convertir. Puede ser null.
     * @return Un {@link SerieDTO} con los datos de la entidad, o null si la entrada es null.
     */
    public static SerieDTO toDTO(Serie serie) {
        if (serie == null) return null;

        return new SerieDTO(
                serie.getId(),
                serie.getNumeroSerie(),
                serie.getRepeticiones(),
                serie.getPeso()
        );
    }

    /**
     * Convierte un {@link SerieDTO} a la entidad Serie correspondiente.
     *
     * @param dto DTO de Serie que se desea convertir. Puede ser null.
     * @return Una nueva entidad {@link Serie} con los valores del DTO, o null si la entrada es null.
     */
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
