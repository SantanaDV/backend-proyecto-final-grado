package org.backend.backendfacilgim.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.backend.backendfacilgim.security.filter.JwtAuthenticationFilter;
import org.backend.backendfacilgim.security.filter.JwtValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.PrintWriter;

@Configuration
@EnableMethodSecurity   // Esto habilita el procesamiento de @PreAuthorize
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Genera el AuthenticationManager a partir de la configuración.
     */
    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Define la cadena de filtros y la política de accesos.
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        // JWT Filters
        JwtAuthenticationFilter authFilter = new JwtAuthenticationFilter(authenticationManager());
        JwtValidationFilter validationFilter = new JwtValidationFilter(authenticationManager());

        return http
                // Manejo de 403 Forbidden
                // Configuración de sesiones
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Desabilitar el CSRF ya que solo vamos a trabajar con la API
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        //Rutas a la documentacion pública
                        /*
                        Añadir en caso de hacer el swagger
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs/swagger-config",
                                "/v3/api-docs/public-api",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
*/

                        .requestMatchers(HttpMethod.POST, "/login", "/api/usuarios/registrar").permitAll()
                                .anyRequest().permitAll()


                        // Cualquier otra ruta requiere autenticación
                        //  Quitado mientras pruebo rutas
                       // .anyRequest().authenticated()
                )

// Manejo de excepciones de autenticación y autorización
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint()) // Manejo de 401 Unauthorized
                        .accessDeniedHandler(customAccessDeniedHandler())
                )
                .addFilter(validationFilter)
                .addFilterBefore(authFilter, JwtValidationFilter.class)


                .build();
    }

    /**
     * Personaliza la respuesta para 401 Unauthorized (cuando el usuario NO está autenticado).
     */
    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write("{\"error\": \"No estás autenticado. Por favor, inicia sesión.\"}");
            writer.flush();
        };
    }

    /**
     * Personaliza la respuesta para 403 Forbidden (cuando el usuario no tiene permisos).
     */
    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write("{\"error\": \"Acceso denegado: No tienes permisos para realizar esta acción.\"}");
            writer.flush();
        };
    }
}