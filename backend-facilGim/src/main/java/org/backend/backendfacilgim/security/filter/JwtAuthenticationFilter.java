package org.backend.backendfacilgim.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import java.util.HashMap;
import java.util.Map;

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
        } catch (IOException e) {
            throw new AuthenticationServiceException("Error leyendo credenciales", e);
        }
        // Creamos el token de autenticación
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
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();


        // Pasar roles y username en el token
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", roles.stream().map(GrantedAuthority::getAuthority).toList());
        claims.put("username", username);


        String token = Jwts.builder()
                .setSubject(username)
                .addClaims(claims)  //
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(SECRET_KEY)
                .compact();


        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

        Map<String, String> body = new HashMap<>();
        body.put("token", token);
        body.put("username", username);
        body.put("mensaje", "Has iniciado sesión con éxito!");


        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {
        Map<String, String> body = new HashMap<>();
        body.put("message", "Error en la autenticación, usuario o password incorrectos!");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(CONTENT_TYPE);
    }
}