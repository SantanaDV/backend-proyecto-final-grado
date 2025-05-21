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
    private EjercicioDTO ejercicio;
    private int orden;

    private List<SerieDTO> series;
}

