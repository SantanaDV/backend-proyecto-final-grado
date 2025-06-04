package org.backend.backendfacilgim.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.backend.backendfacilgim.security.TokenJwtConfig.*;

/**
 * Filtro que valida el token JWT en cada petición protegida.
 * <p>
 * Extrae el token del header "Authorization", lo valida y
 * establece la autenticación en el contexto de seguridad si es válido.
 * En caso de token inválido o ausente, delega o responde con 401.
 *
 * @author Francisco Santana
 */
public class JwtValidationFilter extends BasicAuthenticationFilter {

    /**
     * Constructor que recibe el {@link AuthenticationManager}.
     *
     * @param authenticationManager gestor de autenticación de Spring Security
     */
    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * Método principal que intercepta las peticiones HTTP.
     * <ol>
     *     <li>Lee el header "Authorization" y verifica que comience con el prefijo correcto.</li>
     *     <li>Si no hay token, continúa con el siguiente filtro en la cadena.</li>
     *     <li>Si hay token, lo parsea y valida con la clave secreta.</li>
     *     <li>Extrae el nombre de usuario y la lista de roles de los claims.</li>
     *     <li>Convierte los roles a {@link GrantedAuthority} y establece la autenticación
     *     en el {@link SecurityContextHolder}.</li>
     *     <li>Si el token es inválido o falta información, devuelve 401 con un mensaje de error.</li>
     * </ol>
     *
     * @param request  petición HTTP entrante
     * @param response respuesta HTTP
     * @param chain    cadena de filtros
     * @throws IOException      si ocurre un error I/O al escribir en la respuesta
     * @throws ServletException si ocurre un error en el procesamiento del filtro
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(HEADER_AUTHORIZATION);
        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(PREFIX_TOKEN, "").trim();
        try {
            Jws<Claims> jwsClaims = Jwts.parser()
                    .verifyWith(SECRET_KEY).build()
                    .parseSignedClaims(token);

            Claims claims = jwsClaims.getPayload();
            String username = claims.getSubject();

            // Extraer la lista de roles correctamente desde los claims
            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("authorities", List.class);

            if (username == null || roles == null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            // Convertir los roles a Collection<GrantedAuthority>
            Collection<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Autenticamos el usuario con los roles correctos
            SecurityContextHolder.getContext().setAuthentication(
                    new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                            username, null, authorities));

            chain.doFilter(request, response);

        } catch (JwtException e) {
            Map<String, String> body = Map.of(
                    "error", e.getMessage(),
                    "message", "El token JWT no es válido"
            );
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CONTENT_TYPE);
        }
    }
}
