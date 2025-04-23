package org.backend.backendfacilgim.mapper;

import lombok.experimental.UtilityClass;
import org.backend.backendfacilgim.dto.TipoEntrenamientoDTO;
import org.backend.backendfacilgim.entity.TipoEntrenamiento;

@UtilityClass
public class TipoEntrenamientoMapper {

    public TipoEntrenamientoDTO toDTO(TipoEntrenamiento tipo) {
        TipoEntrenamientoDTO dto = new TipoEntrenamientoDTO();
        dto.setId(tipo.getId());
        dto.setNombre(tipo.getNombre());
        return dto;
    }

    public TipoEntrenamiento toEntity(TipoEntrenamientoDTO dto) {
        TipoEntrenamiento tipo = new TipoEntrenamiento();
        tipo.setId(dto.getId());
        tipo.setNombre(dto.getNombre());
        return tipo;
    }
}
