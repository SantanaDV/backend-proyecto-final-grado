package org.backend.backendfacilgim.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class ImagenController {

    private static final Logger logger = LoggerFactory.getLogger(ImagenController.class);

    @GetMapping("/imagen/{nombreImagen}")
    public ResponseEntity<Resource> obtenerImagen(@PathVariable String nombreImagen) {
        Path ruta = Path.of("src/main/resources/static/uploads/" + nombreImagen);

        if (!Files.exists(ruta)) {
            logger.warn("Imagen no encontrada: {}", ruta.toAbsolutePath());
            return ResponseEntity.notFound().build();
        }

        String contentType;
        try {
            contentType = Files.probeContentType(ruta);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        } catch (IOException e) {
            logger.error("No se pudo determinar el tipo MIME de la imagen", e);
            contentType = "application/octet-stream";
        }

        Resource resource = new FileSystemResource(ruta);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }
}
