package org.backend.backendfacilgim.testController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.backend.backendfacilgim.config.TestSecurityConfig;
import org.backend.backendfacilgim.controller.EjercicioController;
import org.backend.backendfacilgim.dto.AsignacionConSeriesDTO;
import org.backend.backendfacilgim.dto.ActualizacionInstanciaDTO;
import org.backend.backendfacilgim.dto.EjercicioDTO;
import org.backend.backendfacilgim.dto.EjercicioDeleteDTO;
import org.backend.backendfacilgim.dto.SerieDTO;
import org.backend.backendfacilgim.entity.Ejercicio;
import org.backend.backendfacilgim.service.EjercicioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EjercicioController.class)
@Import({EjercicioControllerTest.MockConfig.class, TestSecurityConfig.class})
public class EjercicioControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private EjercicioService ejercicioService;

    @BeforeEach
    void setUp() {
        Mockito.reset(ejercicioService);
    }

    @Test
    void GET_ListarEjercicios() throws Exception {
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setIdEjercicio(1);
        ejercicio.setNombre("Sentadilla");

        Mockito.when(ejercicioService.listarEjercicios()).thenReturn(List.of(ejercicio));

        mockMvc.perform(get("/api/ejercicios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Sentadilla"));
    }

    @Test
    void GET_ObtenerPorId() throws Exception {
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setIdEjercicio(1);
        ejercicio.setNombre("Press Banca");

        Mockito.when(ejercicioService.getEjercicio(1))
                .thenReturn(Optional.of(ejercicio));

        mockMvc.perform(get("/api/ejercicios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Press Banca"));
    }

    @Test
    void POST_CrearEjercicio_SinImagen() throws Exception {
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setNombre("Curl Biceps");

        MockMultipartFile ejercicioJson = new MockMultipartFile(
                "ejercicio","", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(ejercicio)
        );

        Mockito.when(ejercicioService.crearEjercicio(any(Ejercicio.class)))
                .thenReturn(ejercicio);

        mockMvc.perform(multipart("/api/ejercicios")
                        .file(ejercicioJson)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Curl Biceps"));
    }

    @Test
    void POST_CrearEjercicio_ConImagen() throws Exception {
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setNombre("Curl Biceps");

        MockMultipartFile ejercicioJson = new MockMultipartFile(
                "ejercicio","", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(ejercicio)
        );
        MockMultipartFile imagen = new MockMultipartFile(
                "imagen","imagen.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fakeimage".getBytes()
        );

        Mockito.when(ejercicioService.crearEjercicio(any(Ejercicio.class)))
                .thenReturn(ejercicio);

        mockMvc.perform(multipart("/api/ejercicios")
                        .file(ejercicioJson)
                        .file(imagen)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Curl Biceps"));
    }

    @Test
    void POST_ActualizarEjercicio_SinImagen_MantenerImagenAnterior() throws Exception {
        Ejercicio actualizado = new Ejercicio();
        actualizado.setIdEjercicio(2);
        actualizado.setNombre("Remo Nuevo");
        actualizado.setImagenUrl("anterior.jpg");

        Mockito.when(ejercicioService.actualizarEjercicio(eq(2), any(Ejercicio.class)))
                .thenReturn(actualizado);

        MockMultipartFile ejercicioJson = new MockMultipartFile(
                "ejercicio","", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(actualizado)
        );

        mockMvc.perform(multipart("/api/ejercicios")
                        .file(ejercicioJson)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.imagenUrl").value("anterior.jpg"))
                .andExpect(jsonPath("$.nombre").value("Remo Nuevo"));
    }

    @Test
    void DELETE_EliminarPorId() throws Exception {
        Mockito.doNothing().when(ejercicioService).eliminarEjercicio(1);

        mockMvc.perform(delete("/api/ejercicios/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void DELETE_EliminarPorNombre() throws Exception {
        EjercicioDeleteDTO dto = new EjercicioDeleteDTO();
        dto.setNombre("Remo");
        dto.setUsernamePropietario("usuario1");

        Mockito.doNothing().when(ejercicioService)
                .eliminarEjercicioPorNombre("Remo", "usuario1");

        mockMvc.perform(delete("/api/ejercicios/nombre")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void POST_AsignarAEentrenamientoConSeries() throws Exception {
        AsignacionConSeriesDTO body = new AsignacionConSeriesDTO();
        body.setEjercicioId(5);
        // lista de Series correcta
        List<SerieDTO> series = List.of(new SerieDTO(null, 1, 3, 50.0));
        body.setSeries(series);
        body.setOrden(1);

        EjercicioDTO dto = new EjercicioDTO();
        dto.setIdEjercicio(10);
        dto.setNombre("Flexiones");

        Mockito.when(ejercicioService
                        .asignarEjercicioConSeriesAEntrenamiento(eq(7), eq(5), eq(series), eq(1)))
                .thenReturn(dto);

        mockMvc.perform(post("/api/ejercicios/entrenamiento/7/asignar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEjercicio").value(10))
                .andExpect(jsonPath("$.nombre").value("Flexiones"));
    }

    @Test
    void PUT_ActualizarInstanciaEnEntrenamiento() throws Exception {
        ActualizacionInstanciaDTO body = new ActualizacionInstanciaDTO();
        List<SerieDTO> series = List.of(new SerieDTO(null, 1, 8, 60.0));
        body.setSeries(series);
        body.setOrden(2);

        EjercicioDTO dto = new EjercicioDTO();
        dto.setIdEjercicio(11);
        dto.setNombre("Dominadas");

        Mockito.when(ejercicioService
                        .actualizarInstanciaConSeries(eq(9), eq(series), eq(2)))
                .thenReturn(dto);

        mockMvc.perform(put("/api/ejercicios/entrenamiento/ejercicio/9")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEjercicio").value(11))
                .andExpect(jsonPath("$.nombre").value("Dominadas"));
    }

    @Test
    void DELETE_EliminarInstanciaEnEntrenamiento() throws Exception {
        Mockito.doNothing().when(ejercicioService).eliminarInstancia(12);

        mockMvc.perform(delete("/api/ejercicios/entrenamiento/ejercicio/12")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @TestConfiguration
    static class MockConfig {
        @Bean public EjercicioService ejercicioService() {
            return Mockito.mock(EjercicioService.class);
        }
    }
}
