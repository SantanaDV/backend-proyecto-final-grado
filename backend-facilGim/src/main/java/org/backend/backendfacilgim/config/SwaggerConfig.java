package org.backend.backendfacilgim.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Springdoc OpenAPI (Swagger) para la aplicación.
 * <p>
 * Define los beans necesarios para generar la documentación interactiva
 * de la API, accesible en {@code /swagger-ui.html} y los endpoints de OpenAPI.
 * </p>
 *
 * Autor: Francisco Santana
 */
@Configuration
public class SwaggerConfig {

    /**
     * Bean principal que configura la información básica de la API:
     * título, descripción, versión y documentación externa.
     * <p>
     * Esta configuración se mostrará en la interfaz de Swagger UI
     * y en el archivo JSON de OpenAPI disponible en {@code /api-docs}.
     * </p>
     *
     * @return instancia de {@link OpenAPI} con la información de la API.
     */
    @Bean
    public OpenAPI facilGimOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FacilGim API")
                        .description("API para la gestión de rutinas de entrenamiento, usuarios y roles.")
                        .version("v1.0.0")
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación Externa")
                        .url("https://github.com/SantanaDV/backend-proyecto-final-grado")
                );
    }

    /**
     * Bean que agrupa las rutas que deben incluirse en la documentación pública.
     * <p>
     * Define un grupo llamado {@code public-api} que incluirá únicamente
     * las rutas que coincidan con el patrón {@code /api/**}.
     * </p>
     *
     * @return instancia de {@link GroupedOpenApi} configurada para las rutas públicas.
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/api/**")
                .build();
    }
}
