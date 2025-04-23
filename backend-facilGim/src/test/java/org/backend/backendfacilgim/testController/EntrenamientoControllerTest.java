package org.backend.backendfacilgim.testController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.backend.backendfacilgim.config.TestSecurityConfig;
import org.backend.backendfacilgim.controller.EntrenamientoController;
import org.backend.backendfacilgim.dto.EntrenamientoDTO;
import org.backend.backendfacilgim.entity.Entrenamiento;
import org.backend.backendfacilgim.entity.TipoEntrenamiento;
import org.backend.backendfacilgim.entity.Usuario;
import org.backend.backendfacilgim.service.EntrenamientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(controllers = EntrenamientoController.class)
@Import({EntrenamientoControllerTest.MockConfig.class, TestSecurityConfig.class})
public class EntrenamientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntrenamientoService entrenamientoService;

    @BeforeEach
    void setUp() {
        Mockito.reset(entrenamientoService);
    }

    @Test
    void GET_ListarTodos() throws Exception {
        Entrenamiento ent = new Entrenamiento();
        ent.setNombre("Piernas");

        Mockito.when(entrenamientoService.obtenerTodosLosEntrenamientos()).thenReturn(List.of(ent));

        mockMvc.perform(get("/api/entrenamientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Piernas"));
    }

    @Test
    void GET_EntrenamientosPorFechas() throws Exception {
        Mockito.when(entrenamientoService.obtenerEntrenamientosEntreDosFechas(any(), any()))
                .thenReturn(List.of(new Entrenamiento()));

        mockMvc.perform(get("/api/entrenamientos/fecha")
                        .param("fechaInicio", "2024-01-01")
                        .param("fechaFin", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void GET_PorId_Existente() throws Exception {
        Entrenamiento ent = new Entrenamiento();
        ent.setIdEntrenamiento(1);
        ent.setNombre("Pecho");

        Mockito.when(entrenamientoService.obtenerEntrenamientoPorId(1)).thenReturn(Optional.of(ent));

        mockMvc.perform(get("/api/entrenamientos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pecho"));
    }

    @Test
    void GET_PorId_Inexistente() throws Exception {
        Mockito.when(entrenamientoService.obtenerEntrenamientoPorId(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/entrenamientos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void POST_CrearEntrenamiento() throws Exception {
        Entrenamiento ent = new Entrenamiento();
        ent.setNombre("Espalda");
        ent.setDescripcion("Entrenamiento de espalda");
        ent.setDuracion(45);
        ent.setFechaEntrenamiento(LocalDate.now());

        // Mock entidades necesarias
        ent.setTipoEntrenamiento(new TipoEntrenamiento(1, "Fuerza"));
        ent.setUsuario(new Usuario(1, "user", "pass", "user@example.com"));

        Mockito.when(entrenamientoService.crearEntrenamiento(any())).thenReturn(ent);

        mockMvc.perform(post("/api/entrenamientos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Espalda"));
    }


    @Test
    void PUT_ActualizarPorId() throws Exception {
        Entrenamiento ent = new Entrenamiento();
        ent.setNombre("Piernas Avanzado");
        ent.setDescripcion("Entrenamiento avanzado de piernas");
        ent.setDuracion(60);
        ent.setFechaEntrenamiento(LocalDate.now());
        ent.setTipoEntrenamiento(new TipoEntrenamiento(1, "Fuerza"));
        ent.setUsuario(new Usuario(1, "user", "pass", "user@example.com"));

        Mockito.when(entrenamientoService.actualizarEntrenamiento(eq(1), any()))
                .thenReturn(ent);

        mockMvc.perform(put("/api/entrenamientos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Piernas Avanzado"));
    }

    @Test
    void PUT_ActualizarPorNombre() throws Exception {
        Entrenamiento ent = new Entrenamiento();
        ent.setNombre("Hombros");
        ent.setDescripcion("Entrenamiento de hombros");
        ent.setDuracion(40);
        ent.setFechaEntrenamiento(LocalDate.now());
        ent.setTipoEntrenamiento(new TipoEntrenamiento(1, "Fuerza"));
        ent.setUsuario(new Usuario(1, "user", "pass", "user@example.com"));

        Mockito.when(entrenamientoService.actualizarEntrenamientoPorNombre(eq("base"), any()))
                .thenReturn(ent);

        mockMvc.perform(put("/api/entrenamientos/nombre/base")
                        .with(csrf())
                        .param("username", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Hombros"));
    }

    @Test
    void PUT_ActualizarDesdeDTO() throws Exception {
        Entrenamiento ent = new Entrenamiento();
        ent.setNombre("Core");

        Mockito.when(entrenamientoService.actualizarEntrenamientoDesdeDTO(eq(1), any()))
                .thenReturn(ent);

        EntrenamientoDTO dto = new EntrenamientoDTO();
        dto.setNombre("Core");
        dto.setDescripcion("Entrenamiento de core completo");
        dto.setDuracion(30);
        dto.setFechaEntrenamiento(LocalDate.now());
        dto.setTipoEntrenamientoId(1L);
        dto.setUsuarioId(1);
        dto.setEjerciciosId(List.of(101)); // Un ejercicio dummy

        mockMvc.perform(put("/api/entrenamientos/dto/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Core"));
    }


    @Test
    void DELETE_EntrenamientoPorId() throws Exception {
        Mockito.doNothing().when(entrenamientoService).eliminarEntrenamiento(1);

        mockMvc.perform(delete("/api/entrenamientos/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void DELETE_EntrenamientoPorNombre() throws Exception {
        Mockito.doNothing().when(entrenamientoService).eliminarEntrenamientoPorNombre("fuerza");

        mockMvc.perform(delete("/api/entrenamientos/nombre/fuerza")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public EntrenamientoService entrenamientoService() {
            return Mockito.mock(EntrenamientoService.class);
        }
    }
}
