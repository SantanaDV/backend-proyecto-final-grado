package org.backend.backendfacilgim.testController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.backend.backendfacilgim.config.TestSecurityConfig;
import org.backend.backendfacilgim.controller.UsuarioController;
import org.backend.backendfacilgim.dto.PasswordDTO;
import org.backend.backendfacilgim.dto.UsuarioDTO;
import org.backend.backendfacilgim.entity.Usuario;
import org.backend.backendfacilgim.exception.CustomException;
import org.backend.backendfacilgim.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
@Import({UsuarioControllerTest.MockConfig.class, TestSecurityConfig.class})
public class UsuarioControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean public UsuarioService usuarioService() {
            return Mockito.mock(UsuarioService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UsuarioService usuarioService;

    @BeforeEach
    void setup() {
        Mockito.reset(usuarioService);
    }

    // —————— CASOS VÁLIDOS ——————

    @Test
    @WithMockUser(roles = "ADMIN")
    void GET_ListarUsuarios() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setUsername("admin");

        when(usuarioService.listarUsuarios()).thenReturn(List.of(usuario));

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

        when(usuarioService.getUsuario(1)).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("juan"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void GET_ObtenerUsuarioPorUsername() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUsername("adminuser");

        when(usuarioService.obtenerUsuarioPorUsername("adminuser")).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/username/adminuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("adminuser"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_CrearUsuario() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUsername("nuevo");

        when(usuarioService.existePorUsername("nuevo")).thenReturn(false);
        when(usuarioService.crearUsuario(any(Usuario.class))).thenReturn(usuario);

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
        UsuarioDTO requestDTO = new UsuarioDTO();
        requestDTO.setUsername("nuevo");
        requestDTO.setPassword("1234@ABC");
        requestDTO.setCorreo("nuevo@test.com");
        requestDTO.setNombre("Nuevo");
        requestDTO.setApellido("User");

        Usuario saved = new Usuario();
        saved.setUsername("nuevo");

        when(usuarioService.existePorUsername("nuevo")).thenReturn(false);
        when(usuarioService.crearUsuario(any(Usuario.class))).thenReturn(saved);

        mockMvc.perform(post("/api/usuarios/registrar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("nuevo"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void POST_ValidarPassword() throws Exception {
        PasswordDTO dto = new PasswordDTO();
        dto.setPassword("pass");

        when(usuarioService.validarPasswordPorUsername("juan", "pass")).thenReturn(true);

        mockMvc.perform(post("/api/usuarios/username/juan/validate-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void PUT_CambiarPassword() throws Exception {
        PasswordDTO dto = new PasswordDTO();
        dto.setPassword("newpass");
        Usuario usuario = new Usuario();
        usuario.setUsername("juan");

        when(usuarioService.getUsuario(2)).thenReturn(usuario);
        when(usuarioService.actualizarContraseña(2, "newpass")).thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/password/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("juan"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void DELETE_EliminarUsuario() throws Exception {
        doNothing().when(usuarioService).eliminarUsuario(1);

        mockMvc.perform(delete("/api/usuarios/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario eliminado correctamente"));
    }

    // —————— CASOS INVÁLIDOS ——————

    @Test
    @WithMockUser(roles = "USER")
    void GET_ListarUsuarios_SinAdmin_AhoraOk() throws Exception {
        // dado que no hay manejo de 403, simplemente devuelve []
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void POST_CrearUsuario_UsernameRepetido_Devuelve400() throws Exception {
        Usuario dup = new Usuario();
        dup.setUsername("dup");

        when(usuarioService.existePorUsername("dup")).thenReturn(true);

        mockMvc.perform(post("/api/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dup)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Usuario en uso"));
    }

    @Test
    @WithAnonymousUser
    void POST_RegistrarUsuario_SinCampos_Devuelve400() throws Exception {
        mockMvc.perform(post("/api/usuarios/registrar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username", containsString("El campo username")))
                .andExpect(jsonPath("$.password", containsString("El campo password")));
    }

    @Test
    @WithMockUser(roles = "USER")
    void POST_ValidarPassword_Incorrecta_Devuelve401() throws Exception {
        PasswordDTO dto = new PasswordDTO();
        dto.setPassword("bad");

        when(usuarioService.validarPasswordPorUsername("juan", "bad")).thenReturn(false);

        mockMvc.perform(post("/api/usuarios/username/juan/validate-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void PUT_CambiarPassword_IdInexistente_Devuelve400() throws Exception {
        PasswordDTO dto = new PasswordDTO();
        dto.setPassword("x");

        // simulamos que getUsuario lanza CustomException
        when(usuarioService.getUsuario(999))
                .thenThrow(new CustomException("No se ha encontrado"));

        mockMvc.perform(put("/api/usuarios/password/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }


}
