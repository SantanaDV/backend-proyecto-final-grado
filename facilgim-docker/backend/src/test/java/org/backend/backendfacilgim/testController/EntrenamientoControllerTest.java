package org.backend.backendfacilgim.testController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.backend.backendfacilgim.config.TestSecurityConfig;
import org.backend.backendfacilgim.controller.EntrenamientoController;
import org.backend.backendfacilgim.dto.EntrenamientoDTO;
import org.backend.backendfacilgim.dto.TipoEntrenamientoDTO;
import org.backend.backendfacilgim.dto.UsuarioDTO;
import org.backend.backendfacilgim.entity.Entrenamiento;
import org.backend.backendfacilgim.entity.TipoEntrenamiento;
import org.backend.backendfacilgim.entity.Usuario;
import org.backend.backendfacilgim.service.EntrenamientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        Mockito.when(entrenamientoService.obtenerTodosLosEntrenamientos())
                .thenReturn(List.of(ent));

        mockMvc.perform(get("/api/entrenamientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Piernas"));
    }

    @Test
    void GET_EntrenamientosPorFechas() throws Exception {
        Mockito.when(entrenamientoService.obtenerEntrenamientosEntreDosFechas(any(LocalDate.class), any(LocalDate.class)))
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

        Mockito.when(entrenamientoService.obtenerEntrenamientoPorId(1))
                .thenReturn(Optional.of(ent));

        mockMvc.perform(get("/api/entrenamientos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pecho"));
    }

    @Test
    void GET_PorId_Inexistente() throws Exception {
        Mockito.when(entrenamientoService.obtenerEntrenamientoPorId(999))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/entrenamientos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void GET_PorUsuarioId() throws Exception {
        Mockito.when(entrenamientoService.encontrarEntrenamientoPorIdUsuario(5))
                .thenReturn(List.of(new Entrenamiento()));

        mockMvc.perform(get("/api/entrenamientos/usuarioId/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void GET_PorNombre() throws Exception {
        Entrenamiento ent = new Entrenamiento();
        ent.setNombre("Cardio");
        Mockito.when(entrenamientoService.obtenerEntrenamientosPorNombre("Cardio"))
                .thenReturn(List.of(ent));

        mockMvc.perform(get("/api/entrenamientos/nombre/Cardio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Cardio"));
    }

    @Test
    void POST_CrearEntrenamiento() throws Exception {
        Entrenamiento ent = new Entrenamiento();
        ent.setNombre("Espalda");
        ent.setDescripcion("Entrenamiento de espalda");
        ent.setDuracion(45);
        ent.setFechaEntrenamiento(LocalDate.now());
        ent.setTipoEntrenamiento(new TipoEntrenamiento(1, "Fuerza"));
        ent.setUsuario(new Usuario(1, "user", "pass", "user@example.com"));

        // Construcción del DTO con los tipos correctos
        EntrenamientoDTO dto = new EntrenamientoDTO();
        dto.setNombre("Espalda");
        dto.setDescripcion("Entrenamiento de espalda");
        dto.setDuracion(45);
        dto.setFechaEntrenamiento(LocalDate.now());
        dto.setTipoEntrenamiento(new TipoEntrenamientoDTO(1L, "Fuerza"));
        dto.setUsuario(new UsuarioDTO(1, "user", null, "user@example.com", "", "", "", false));
        dto.setEjerciciosId(List.of(101));
        dto.setEntrenamientosEjercicios(null);

        Mockito.when(entrenamientoService.crearDesdeDTO(any(EntrenamientoDTO.class)))
                .thenReturn(ent);

        mockMvc.perform(post("/api/entrenamientos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
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

        Mockito.when(entrenamientoService.actualizarEntrenamiento(eq(1), any(Entrenamiento.class)))
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

        Mockito.when(entrenamientoService.actualizarEntrenamientoPorNombre(eq("base"), any(Entrenamiento.class)))
                .thenReturn(ent);

        mockMvc.perform(put("/api/entrenamientos/nombre/base")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Hombros"));
    }

    @Test
    void PUT_ActualizarDesdeDTO() throws Exception {
        Entrenamiento ent = new Entrenamiento();
        ent.setNombre("Core");

        EntrenamientoDTO dto = new EntrenamientoDTO();
        dto.setNombre("Core");
        dto.setDescripcion("Entrenamiento de core completo");
        dto.setDuracion(30);
        dto.setFechaEntrenamiento(LocalDate.now());
        dto.setTipoEntrenamiento(new TipoEntrenamientoDTO(1L, "Fuerza"));
        dto.setUsuario(new UsuarioDTO(1, "user", null, "user@example.com", "", "", "", false));
        dto.setEjerciciosId(List.of(101));
        dto.setEntrenamientosEjercicios(null);

        Mockito.when(entrenamientoService.actualizarEntrenamientoDesdeDTO(eq(1), any(EntrenamientoDTO.class)))
                .thenReturn(ent);

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
