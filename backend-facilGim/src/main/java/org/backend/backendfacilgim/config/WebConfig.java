package org.backend.backendfacilgim.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Configuración de recursos estáticos para servir archivos cargados.
 * Hace que los archivos almacenados en el directorio local "uploads"
 * sean accesibles mediante la ruta {@code /uploads/**} en la aplicación.
 *
 *  @author Francisco Santana
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Registra un recurso estático que mapea todas las peticiones a {@code /uploads/**}
     * al directorio físico "uploads" en el sistema de archivos.
     * Esto permite que, por ejemplo, la URL {@code /uploads/imagen.jpg} sirva
     * el archivo {@code uploads/imagen.jpg} ubicado en el disco.
     *
     * @param registry Registro de recursos al que se añaden los mapeos.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Paths.get("uploads").toAbsolutePath().toString();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");

        registry
                .addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/4.15.5/");

        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
