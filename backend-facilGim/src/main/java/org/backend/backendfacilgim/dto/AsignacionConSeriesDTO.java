package org.backend.backendfacilgim.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AsignacionConSeriesDTO {

    @NotNull
    private Integer ejercicioId;

    private Integer orden;

    @NotNull
    private List<SerieDTO> series;

    public Integer getEjercicioId() {
        return ejercicioId;
    }

    public void setEjercicioId(Integer ejercicioId) {
        this.ejercicioId = ejercicioId;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public List<SerieDTO> getSeries() {
        return series;
    }

    public void setSeries(List<SerieDTO> series) {
        this.series = series;
    }
}
