package org.backend.backendfacilgim.testController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.backend.backendfacilgim.config.TestSecurityConfig;
import org.backend.backendfacilgim.controller.EjercicioController;
import org.backend.backendfacilgim.dto.EjercicioDTO;
import org.backend.backendfacilgim.dto.EjercicioDeleteDTO;
import org.backend.backendfacilgim.entity.Ejercicio;
import org.backend.backendfacilgim.service.EjercicioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(controllers = EjercicioController.class)
@Import({EjercicioControllerTest.MockConfig.class, TestSecurityConfig.class})
public class EjercicioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EjercicioService ejercicioService;

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

        Mockito.when(ejercicioService.getEjercicio(1)).thenReturn(Optional.of(ejercicio));

        mockMvc.perform(get("/api/ejercicios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Press Banca"));
    }

    @Test
    void POST_CrearEjercicio_SinImagen() throws Exception {
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setNombre("Curl Biceps");

        MockMultipartFile ejercicioJson = new MockMultipartFile("ejercicio", "",
                "application/json", objectMapper.writeValueAsBytes(ejercicio));

        Mockito.when(ejercicioService.crearEjercicio(any())).thenReturn(ejercicio);

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

        MockMultipartFile ejercicioJson = new MockMultipartFile("ejercicio", "",
                "application/json", objectMapper.writeValueAsBytes(ejercicio));

        MockMultipartFile imagen = new MockMultipartFile("imagen", "imagen.jpg",
                MediaType.IMAGE_JPEG_VALUE, "fakeimage".getBytes());

        Mockito.when(ejercicioService.crearEjercicio(any())).thenReturn(ejercicio);

        mockMvc.perform(multipart("/api/ejercicios")
                        .file(ejercicioJson)
                        .file(imagen)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Curl Biceps"));
    }

    @Test
    void POST_ActualizarEjercicio_SinImagen_MantenerImagenAnterior() throws Exception {
        Ejercicio ejercicioExistente = new Ejercicio();
        ejercicioExistente.setIdEjercicio(2);
        ejercicioExistente.setNombre("Remo");
        ejercicioExistente.setImagenUrl("anterior.jpg");

        Ejercicio actualizado = new Ejercicio();
        actualizado.setIdEjercicio(2);
        actualizado.setNombre("Remo Nuevo");
        actualizado.setImagenUrl("anterior.jpg");

        Mockito.when(ejercicioService.getEjercicio(2)).thenReturn(Optional.of(ejercicioExistente));
        Mockito.when(ejercicioService.actualizarEjercicio(eq(2), any())).thenReturn(actualizado);

        MockMultipartFile ejercicioJson = new MockMultipartFile("ejercicio", "",
                "application/json", objectMapper.writeValueAsBytes(actualizado));

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

        Mockito.doNothing().when(ejercicioService).eliminarEjercicioPorNombre("Remo", "usuario1");

        mockMvc.perform(delete("/api/ejercicios/nombre")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public EjercicioService ejercicioService() {
            return Mockito.mock(EjercicioService.class);
        }
    }
}
