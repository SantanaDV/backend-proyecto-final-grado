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

    /**
     * Define el bean de {@link PasswordEncoder} para encriptar contraseñas usando BCrypt.
     *
     * @return instancia de {@link BCryptPasswordEncoder}
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Proporciona el {@link AuthenticationManager} configurado por Spring Security.
     *
     * @return el {@link AuthenticationManager} utilizado en la aplicación
     * @throws Exception en caso de error al obtener el administrador de autenticación
     */
    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura la cadena de filtros de seguridad, incluyendo los filtros JWT de autenticación y validación.
     * <p>
     * - Deshabilita CSRF y establece la gestión de sesión como stateless.<br>
     * - Permite acceso público a endpoints de Swagger, login, registro de usuario y recursos estáticos.<br>
     * - Exige autenticación para el resto de rutas.<br>
     * - Añade filtros para validar y generar tokens JWT.<br>
     * - Configura manejadores personalizados para errores de autenticación y autorización.
     *
     * @param http instancia de {@link HttpSecurity} para configurar reglas de seguridad
     * @return un {@link SecurityFilterChain} con la configuración aplicada
     * @throws Exception en caso de error al construir la cadena de filtros
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Creamos los filtros pasando el usuarioService al authFilter
        JwtAuthenticationFilter authFilter =
                new JwtAuthenticationFilter(authenticationManager(), usuarioService);  // <—
        JwtValidationFilter validationFilter =
                new JwtValidationFilter(authenticationManager());

        return http
                // 1) Gestión de sesiones sin estado (JWT)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 2) Deshabilitar CSRF (no usado en APIs stateless)
                .csrf(csrf -> csrf.disable())
                // 3) Reglas de autorización de solicitudes HTTP
                .authorizeHttpRequests(authz -> authz
                        // Rutas públicas de Swagger
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs/swagger-config",
                                "/v3/api-docs/public-api",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        // Permitir login y registro sin autenticación
                        .requestMatchers(HttpMethod.POST, "/login", "/api/usuarios/registrar").permitAll()
                        // Recursos estáticos de imágenes
                        .requestMatchers("/uploads/**").permitAll()
                        // Liveness probe de Actuator
                        .requestMatchers("/actuator/health").permitAll()
                        // El resto de peticiones requieren estar autenticado
                        .anyRequest().authenticated()
                )
                // 4) Manejadores de excepciones personalizados
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                        .accessDeniedHandler(customAccessDeniedHandler())
                )
                // 5) Añadir filtros: validación del token antes de autenticación, luego filtro de autenticación
                .addFilterBefore(authFilter, JwtValidationFilter.class)
                .addFilter(validationFilter)
                .build();
    }

    /**
     * Punto de entrada personalizado para cuando no se está autenticado.
     * Responde con código 401 y JSON indicando que debe iniciar sesión.
     *
     * @return un {@link AuthenticationEntryPoint} que maneja respuestas 401
     */
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

    /**
     * Manejador personalizado para casos de "acceso denegado".
     * Responde con código 403 y JSON indicando falta de permisos.
     *
     * @return un {@link AccessDeniedHandler} que maneja respuestas 403
     */
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
