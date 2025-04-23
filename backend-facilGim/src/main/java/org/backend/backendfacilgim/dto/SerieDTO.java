package org.backend.backendfacilgim.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SerieDTO {

    private Integer id;
    private int numeroSerie;
    private int repeticiones;
    private double peso;
}
