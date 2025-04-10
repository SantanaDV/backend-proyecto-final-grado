package org.backend.backendfacilgim.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para Springdoc OpenAPI (Swagger).
 * Genera la documentación interactiva en /swagger-ui.html y /api-docs.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Define la configuración principal de la documentación de la API:
     * título, descripción, versión, etc.
     *
     * Con este bean, Springdoc construye la documentación OpenAPI
     * que se mostrará en /swagger-ui.html
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

     * Con este bean, se personalizan las rutas que se documentan,
     * y se asigna un nombre de grupo para diferenciarlas, en este caso api/^^.
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/api/**")
                .build();
    }
}
