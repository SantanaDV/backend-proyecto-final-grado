package org.backend.backendfacilgim.testController;

import org.backend.backendfacilgim.config.TestSecurityConfig;
import org.backend.backendfacilgim.controller.ImagenController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(controllers = ImagenController.class)
@Import({ImagenController.class, TestSecurityConfig.class})
public class ImagenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @TempDir
    Path tempDir;


    private Path uploadsDir;

    @BeforeEach
    void setUp() throws IOException {
        // Crea un directorio temporal que simula el upload folder
        uploadsDir = tempDir.resolve("src/main/resources/static/uploads/");
        Files.createDirectories(uploadsDir);
        System.setProperty("user.dir", tempDir.toFile().getAbsolutePath()); // Para que Path.of(...) funcione correctamente

        //Simula la creación de una imagen en el upload folder
        Path testImagePath = Paths.get("src/main/resources/static/uploads/test-image.jpg");
        Files.createDirectories(testImagePath.getParent());
        Files.write(testImagePath, "fake-image".getBytes());
    }

    @AfterEach
    void tearDown() throws IOException {
        // Elimina la imagen simulada al finalizar cada prueba
        Files.deleteIfExists(Paths.get("src/main/resources/static/uploads/test-image.jpg"));
    }


    @Test
    void GET_Imagen_Existente() throws Exception {
        // Crea una imagen simulada
        String nombreImagen = "test-image.jpg";
        Path imagenPath = uploadsDir.resolve(nombreImagen);
        Files.write(imagenPath, "fake-image".getBytes());

        mockMvc.perform(get("/imagen/" + nombreImagen))
                .andExpect(status().isOk())
                .andExpect(header().exists("Content-Type"))
                .andExpect(content().bytes(Files.readAllBytes(imagenPath)));
    }

    @Test
    void GET_Imagen_No_Existente() throws Exception {
        mockMvc.perform(get("/imagen/no-existe.png"))
                .andExpect(status().isNotFound());
    }
}
