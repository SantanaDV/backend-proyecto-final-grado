package org.backend.backendfacilgim.testController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.backend.backendfacilgim.config.TestSecurityConfig;
import org.backend.backendfacilgim.controller.TipoEntrenamientoController;
import org.backend.backendfacilgim.dto.TipoEntrenamientoDTO;
import org.backend.backendfacilgim.entity.TipoEntrenamiento;
import org.backend.backendfacilgim.service.TipoEntrenamientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TipoEntrenamientoController.class)
@Import({TipoEntrenamientoControllerTest.Config.class, TestSecurityConfig.class})
public class TipoEntrenamientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TipoEntrenamientoService tipoEntrenamientoService;

    @Autowired
    private ObjectMapper objectMapper;

    private TipoEntrenamiento tipo;

    @TestConfiguration
    static class Config {
        @Bean
        public TipoEntrenamientoService tipoEntrenamientoService() {
            return mock(TipoEntrenamientoService.class);
        }
    }

    @BeforeEach
    void setup() {
        tipo = new TipoEntrenamiento();
        tipo.setId(5L);
        tipo.setNombre("Fuerza");
        reset(tipoEntrenamientoService);
    }

    @Test
    void GET_ListarTipos() throws Exception {
        when(tipoEntrenamientoService.listarTipos()).thenReturn(List.of(tipo));

        mockMvc.perform(get("/api/tipos-entrenamiento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5))
                .andExpect(jsonPath("$[0].nombre").value("Fuerza"));
    }

    @Test
    void GET_ObtenerTipoPorId() throws Exception {
        when(tipoEntrenamientoService.obtenerPorId(5L)).thenReturn(tipo);

        mockMvc.perform(get("/api/tipos-entrenamiento/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.nombre").value("Fuerza"));
    }

    @Test
    void POST_CrearTipo() throws Exception {
        TipoEntrenamientoDTO dto = new TipoEntrenamientoDTO();
        dto.setId(5L);
        dto.setNombre("Fuerza");

        when(tipoEntrenamientoService.crearTipo(any())).thenReturn(tipo);

        mockMvc.perform(post("/api/tipos-entrenamiento")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Fuerza"));
    }

    @Test
    void PUT_ActualizarTipo() throws Exception {
        TipoEntrenamientoDTO dto = new TipoEntrenamientoDTO();
        dto.setId(5L);
        dto.setNombre("Fuerza");

        when(tipoEntrenamientoService.actualizarTipo(eq(5L), any())).thenReturn(tipo);

        mockMvc.perform(put("/api/tipos-entrenamiento/5")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Fuerza"));
    }

    @Test
    void DELETE_EliminarTipo() throws Exception {
        doNothing().when(tipoEntrenamientoService).eliminarTipo(2L);

        mockMvc.perform(delete("/api/tipos-entrenamiento/2")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}