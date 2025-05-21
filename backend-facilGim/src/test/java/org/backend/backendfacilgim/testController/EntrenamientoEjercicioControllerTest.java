package org.backend.backendfacilgim.testController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.backend.backendfacilgim.config.TestSecurityConfig;
import org.backend.backendfacilgim.controller.EntrenamientoEjercicioController;
import org.backend.backendfacilgim.dto.EntrenamientoEjercicioDTO;
import org.backend.backendfacilgim.entity.Ejercicio;
import org.backend.backendfacilgim.entity.Entrenamiento;
import org.backend.backendfacilgim.entity.EntrenamientoEjercicio;
import org.backend.backendfacilgim.service.EjercicioService;
import org.backend.backendfacilgim.service.EntrenamientoEjercicioService;
import org.backend.backendfacilgim.service.EntrenamientoService;
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
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(controllers = EntrenamientoEjercicioController.class)
@Import({EntrenamientoEjercicioControllerTest.MockConfig.class, TestSecurityConfig.class})
public class EntrenamientoEjercicioControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private EntrenamientoEjercicioService entrenamientoEjercicioService;
    @Autowired private EntrenamientoService entrenamientoService;
    @Autowired private EjercicioService ejercicioService;

    @BeforeEach
    void setup() {
        Mockito.reset(entrenamientoEjercicioService, entrenamientoService, ejercicioService);
    }

    @Test
    void GET_ListarTodos() throws Exception {
        EntrenamientoEjercicio relacion = new EntrenamientoEjercicio();
        relacion.setId(1);

        Mockito.when(entrenamientoEjercicioService.obtenerTodos()).thenReturn(List.of(relacion));

        mockMvc.perform(get("/api/entrenamiento-ejercicio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void GET_ObtenerPorId() throws Exception {
        EntrenamientoEjercicio relacion = new EntrenamientoEjercicio();
        relacion.setId(1);

        Mockito.when(entrenamientoEjercicioService.obtenerPorId(1)).thenReturn(relacion);

        mockMvc.perform(get("/api/entrenamiento-ejercicio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    /*@Test
    void POST_CrearRelacion() throws Exception {
        EntrenamientoEjercicioDTO dto = new EntrenamientoEjercicioDTO();
        dto.setIdEntrenamiento(1);
       // dto.setIdEjercicio(2);

        Entrenamiento entrenamiento = new Entrenamiento();
        entrenamiento.setIdEntrenamiento(1);

        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setIdEjercicio(2);

        EntrenamientoEjercicio saved = new EntrenamientoEjercicio();
        saved.setId(10);
        saved.setEntrenamiento(entrenamiento);
        saved.setEjercicio(ejercicio);

        Mockito.when(entrenamientoService.obtenerEntrenamientoPorId(1)).thenReturn(Optional.of(entrenamiento));
        Mockito.when(ejercicioService.getEjercicio(2)).thenReturn(Optional.of(ejercicio));
        Mockito.when(entrenamientoEjercicioService.crear(any())).thenReturn(saved);

        mockMvc.perform(post("/api/entrenamiento-ejercicio")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void DELETE_EliminarPorId() throws Exception {
        Mockito.doNothing().when(entrenamientoEjercicioService).eliminarPorId(1);

        mockMvc.perform(delete("/api/entrenamiento-ejercicio/1").with(csrf()))
                .andExpect(status().isNoContent());
    }*/

    @Test
    void GET_ListarPorEntrenamiento() throws Exception {
        EntrenamientoEjercicio relacion = new EntrenamientoEjercicio();
        relacion.setId(5);

        Mockito.when(entrenamientoEjercicioService.obtenerPorIdEntrenamiento(1)).thenReturn(List.of(relacion));

        mockMvc.perform(get("/api/entrenamiento-ejercicio/entrenamiento/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5));
    }

    @TestConfiguration
    static class MockConfig {
        @Bean public EntrenamientoEjercicioService entrenamientoEjercicioService() {
            return Mockito.mock(EntrenamientoEjercicioService.class);
        }

        @Bean public EntrenamientoService entrenamientoService() {
            return Mockito.mock(EntrenamientoService.class);
        }

        @Bean public EjercicioService ejercicioService() {
            return Mockito.mock(EjercicioService.class);
        }
    }
}
