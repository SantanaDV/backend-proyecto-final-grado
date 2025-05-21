package org.backend.backendfacilgim.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EjercicioDTO {

    private Integer idEjercicio;
    private String nombre;
    private String imagenUrl;
    private List<SerieDTO> series;


}