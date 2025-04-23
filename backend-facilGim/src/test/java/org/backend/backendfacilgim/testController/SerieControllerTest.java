package org.backend.backendfacilgim.testController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.backend.backendfacilgim.config.TestSecurityConfig;
import org.backend.backendfacilgim.controller.SerieController;
import org.backend.backendfacilgim.dto.SerieDTO;
import org.backend.backendfacilgim.entity.Serie;
import org.backend.backendfacilgim.service.SerieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(controllers = SerieController.class)
@Import({SerieControllerTest.MockConfig.class, TestSecurityConfig.class})
class SerieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SerieService serieService;

    @BeforeEach
    void setUp() {
        Mockito.reset(serieService);
    }

    @Test
    void GET_ListarSeriesPorEntrenamientoEjercicio() throws Exception {
        Serie serie = new Serie();
        serie.setId(1);
        serie.setNumeroSerie(1);
        serie.setRepeticiones(10);
        serie.setPeso(50);

        when(serieService.listarPorEntrenamientoEjercicio(1)).thenReturn(List.of(serie));

        mockMvc.perform(get("/api/series/entrenamiento-ejercicio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].numeroSerie").value(1));
    }

    @Test
    void POST_CrearSerie() throws Exception {
        SerieDTO dto = new SerieDTO(null, 1, 12, 60.0);
        Serie serie = new Serie();
        serie.setId(1);
        serie.setNumeroSerie(1);
        serie.setRepeticiones(12);
        serie.setPeso(60.0);

        when(serieService.crear(any())).thenReturn(serie);

        mockMvc.perform(post("/api/series")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.peso").value(60.0));
    }

    @Test
    void PUT_ActualizarSerie() throws Exception {
        SerieDTO dto = new SerieDTO(null, 2, 15, 70.0);
        Serie serie = new Serie();
        serie.setId(1);
        serie.setNumeroSerie(2);
        serie.setRepeticiones(15);
        serie.setPeso(70.0);

        when(serieService.actualizar(Mockito.eq(1), any())).thenReturn(serie);

        mockMvc.perform(put("/api/series/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroSerie").value(2));
    }

    @Test
    void GET_ObtenerSeriePorId() throws Exception {
        Serie serie = new Serie();
        serie.setId(1);
        serie.setNumeroSerie(3);
        serie.setRepeticiones(20);
        serie.setPeso(80.0);

        when(serieService.obtenerPorId(1)).thenReturn(serie);

        mockMvc.perform(get("/api/series/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repeticiones").value(20));
    }

    @Test
    void DELETE_EliminarSerie() throws Exception {
        Mockito.doNothing().when(serieService).eliminar(1);

        mockMvc.perform(delete("/api/series/1"))
                .andExpect(status().isNoContent());
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public SerieService serieService() {
            return Mockito.mock(SerieService.class);
        }
    }
}
