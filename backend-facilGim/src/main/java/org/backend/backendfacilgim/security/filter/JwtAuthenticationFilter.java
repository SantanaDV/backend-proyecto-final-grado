package org.backend.backendfacilgim.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.backend.backendfacilgim.dto.LoginResponseDTO;
import org.backend.backendfacilgim.entity.Usuario;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.backend.backendfacilgim.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        super.setFilterProcessesUrl("/login");
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        Usuario user;
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
            if (user.getUsername() == null || user.getPassword() == null) {
                throw new AuthenticationServiceException("Faltan credenciales");
            }
        } catch (IOException e) {
            throw new AuthenticationServiceException("Error leyendo credenciales", e);
        }
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

        org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User) authResult.getPrincipal();

        String username = userDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Lista de roles
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Generación del token JWT
        String token = Jwts.builder()
                .setSubject(username)
                .claim("authorities", roles)
                .claim("username", username)
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(SECRET_KEY)
                .compact();

        // Construcción del DTO para la respuesta
        LoginResponseDTO loginResponse = new LoginResponseDTO(
                "Has iniciado sesión con éxito!",
                token,
                username,
                roles
        );

        // Serializamos el DTO a JSON y lo enviamos
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
        new ObjectMapper().writeValue(response.getWriter(), loginResponse);

        // También añadimos el header Authorization
        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, String> body = Map.of(
                "message", "Error en la autenticación, usuario o password incorrectos!",
                "error", failed.getMessage()
        );
        new ObjectMapper().writeValue(response.getWriter(), body);
    }
}
