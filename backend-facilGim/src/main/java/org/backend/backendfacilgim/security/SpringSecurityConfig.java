package org.backend.backendfacilgim.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.backend.backendfacilgim.security.filter.JwtAuthenticationFilter;
import org.backend.backendfacilgim.security.filter.JwtValidationFilter;
import org.backend.backendfacilgim.service.UsuarioService;             // <—
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.PrintWriter;

@Configuration
@EnableMethodSecurity
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired @Lazy
    private UsuarioService usuarioService;    // <— inyectamos el servicio

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Creamos los filtros pasando el usuarioService al authFilter
        JwtAuthenticationFilter authFilter =
                new JwtAuthenticationFilter(authenticationManager(), usuarioService);  // <—
        JwtValidationFilter validationFilter =
                new JwtValidationFilter(authenticationManager());

        return http
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // rutas públicas de Swagger
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs/swagger-config",
                                "/v3/api-docs/public-api",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/login", "/api/usuarios/registrar").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                        .accessDeniedHandler(customAccessDeniedHandler())
                )
                // orden: validación primero, luego autenticación
                .addFilterBefore(authFilter, JwtValidationFilter.class)
                .addFilter(validationFilter)
                .build();
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (HttpServletRequest req, HttpServletResponse res, org.springframework.security.core.AuthenticationException ex) -> {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.setContentType("application/json");
            PrintWriter w = res.getWriter();
            w.write("{\"error\":\"No estás autenticado. Por favor, inicia sesión.\"}");
            w.flush();
        };
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (HttpServletRequest req, HttpServletResponse res, org.springframework.security.access.AccessDeniedException ex) -> {
            res.setStatus(HttpStatus.FORBIDDEN.value());
            res.setContentType("application/json");
            PrintWriter w = res.getWriter();
            w.write("{\"error\":\"Acceso denegado: No tienes permisos para realizar esta acción.\"}");
            w.flush();
        };
    }
}
