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
    private int repeticiones;
    private double peso;
    private String imagenUrl;
    private List<SerieDTO> series;

    public EjercicioDTO(Integer idEjercicio, String nombre, int repeticiones, double peso, String imagenUrl) {
        this.idEjercicio = idEjercicio;
        this.nombre = nombre;
        this.repeticiones = repeticiones;
        this.peso = peso;
        this.imagenUrl = imagenUrl;
    }
}