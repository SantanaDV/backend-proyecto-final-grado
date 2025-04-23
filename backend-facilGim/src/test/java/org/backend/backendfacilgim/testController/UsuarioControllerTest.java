package org.backend.backendfacilgim.testController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.backend.backendfacilgim.controller.UsuarioController;
import org.backend.backendfacilgim.dto.UsuarioDTO;
import org.backend.backendfacilgim.dto.UsuarioRequestDTO;
import org.backend.backendfacilgim.entity.Usuario;
import org.backend.backendfacilgim.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;


@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioService usuarioService;

    @BeforeEach
    void setup() {
        Mockito.reset(usuarioService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void GET_ListarUsuarios() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setUsername("admin");

        Mockito.when(usuarioService.listarUsuarios()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username").value("admin"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void GET_ObtenerUsuarioPorId() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setUsername("juan");

        Mockito.when(usuarioService.getUsuario(1)).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("juan"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void GET_ObtenerUsuarioPorUsername() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUsername("adminuser");

        Mockito.when(usuarioService.obtenerUsuarioPorUsername("adminuser")).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/username/adminuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("adminuser"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_CrearUsuario() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUsername("nuevo");

        Mockito.when(usuarioService.existePorUsername("nuevo")).thenReturn(false);
        Mockito.when(usuarioService.crearUsuario(any())).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(content().string("usuario creado"));
    }

    @Test
    @WithAnonymousUser
    void POST_RegistrarUsuario_Publico() throws Exception {
        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO();
        requestDTO.setUsername("nuevo");
        requestDTO.setPassword("1234@ABC");
        requestDTO.setCorreo("nuevo@test.com");
        requestDTO.setNombre("Nuevo");
        requestDTO.setApellido("User");

        Usuario saved = new Usuario();
        saved.setUsername("nuevo");

        Mockito.when(usuarioService.existePorUsername("nuevo")).thenReturn(false);
        Mockito.when(usuarioService.crearUsuario(any())).thenReturn(saved);

        mockMvc.perform(post("/api/usuarios/registrar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("nuevo"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void PUT_ActualizarUsuario() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUsername("actualizado");

        Mockito.when(usuarioService.actualizarUsuario(eq(1), any())).thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("actualizado"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void DELETE_EliminarUsuario() throws Exception {
        Mockito.doNothing().when(usuarioService).eliminarUsuario(1);

        mockMvc.perform(delete("/api/usuarios/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario eliminado correctamente"));
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public UsuarioService usuarioService() {
            return Mockito.mock(UsuarioService.class);
        }
    }
}
