package org.backend.backendfacilgim.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrenamientoEjercicioDTO {
    @NotNull
    private Integer idEntrenamiento;

    @NotNull
    private Integer idEjercicio;

    private List<SerieDTO> series;
}
