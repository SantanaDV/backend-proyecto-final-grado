package org.backend.backendfacilgim.mapper;

import lombok.experimental.UtilityClass;
import org.backend.backendfacilgim.dto.TipoEntrenamientoDTO;
import org.backend.backendfacilgim.entity.TipoEntrenamiento;

/**
 * Mapper de utilidad para convertir entre la entidad TipoEntrenamiento
 * y su DTO correspondiente.
 *
 * @autor Francisco Santana
 */
@UtilityClass
public class TipoEntrenamientoMapper {

    /**
     * Convierte una entidad {@link TipoEntrenamiento} a su DTO equivalente.
     *
     * @param tipo Entidad de tipo de entrenamiento a convertir.
     * @return Un {@link TipoEntrenamientoDTO} con los datos de la entidad.
     */
    public TipoEntrenamientoDTO toDTO(TipoEntrenamiento tipo) {
        TipoEntrenamientoDTO dto = new TipoEntrenamientoDTO();
        dto.setId(tipo.getId());
        dto.setNombre(tipo.getNombre());
        return dto;
    }

    /**
     * Convierte un {@link TipoEntrenamientoDTO} a la entidad {@link TipoEntrenamiento}.
     *
     * @param dto DTO de tipo de entrenamiento que se desea convertir.
     * @return Una nueva entidad {@link TipoEntrenamiento} con los valores del DTO.
     */
    public TipoEntrenamiento toEntity(TipoEntrenamientoDTO dto) {
        TipoEntrenamiento tipo = new TipoEntrenamiento();
        tipo.setId(dto.getId());
        tipo.setNombre(dto.getNombre());
        return tipo;
    }
}
