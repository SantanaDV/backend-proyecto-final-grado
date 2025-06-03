package org.backend.backendfacilgim.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.backend.backendfacilgim.dto.LoginResponseDTO;
import org.backend.backendfacilgim.entity.Usuario;
import org.backend.backendfacilgim.service.UsuarioService;    // <—
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.backend.backendfacilgim.security.TokenJwtConfig.*;

/**
 * Filtro de autenticación que intercepta las solicitudes de login,
 * valida las credenciales y, en caso de éxito, genera un token JWT.
 * <p>
 * El endpoint de login se expone en "/login". En una autenticación satisfactoria,
 * emite un JSON con el token, los roles, el nombre de usuario y el userId.
 *
 * @author Francisco Santana
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;

    /**
     * Constructor que establece el {@link AuthenticationManager} y el {@link UsuarioService}.
     * Además redefine la URL de procesamiento de autenticación a "/login".
     *
     * @param authenticationManager administrador de autenticación proporcionado por Spring Security
     * @param usuarioService        servicio para obtener datos adicionales del usuario (por ejemplo, su ID)
     */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   UsuarioService usuarioService) {
        super(authenticationManager);
        super.setFilterProcessesUrl("/login");
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
    }

    /**
     * Intenta autenticar al usuario leyendo las credenciales (username y password)
     * desde el body de la solicitud en formato JSON.
     *
     * @param request  petición HTTP con el JSON de credenciales
     * @param response respuesta HTTP
     * @return un objeto {@link Authentication} si las credenciales son válidas
     * @throws AuthenticationException si ocurre un error de lectura o faltan credenciales
     */
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

    /**
     * Se ejecuta cuando la autenticación es exitosa. Genera un token JWT con:
     * - Subject: username
     * - Claims: authorities (roles) y username
     * - Expiración: 30 días
     * Añade el token en el header "Authorization" y devuelve un JSON con
     * el mensaje, token, username, roles y userId.
     *
     * @param request     petición HTTP
     * @param response    respuesta HTTP donde se incluye el token y el JSON
     * @param chain       cadena de filtros (no se utiliza aquí)
     * @param authResult  resultado de la autenticación (contiene los detalles del usuario)
     * @throws IOException      si ocurre un error al escribir en la respuesta
     * @throws ServletException si ocurre un error en el filtro HTTP
     */
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
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(30))) //1 mes
                .signWith(SECRET_KEY)
                .compact();

        // **Obtenemos el Usuario completo para sacar su ID**
        Usuario usuarioEntity = usuarioService.obtenerUsuarioPorUsername(username);
        Integer userId = usuarioEntity.getIdUsuario();

        // Construcción del DTO para la respuesta, ahora con userId
        LoginResponseDTO loginResponse = new LoginResponseDTO(
                "Has iniciado sesión con éxito!",
                token,
                username,
                roles,
                userId
        );

        // Serializamos el DTO a JSON y lo enviamos
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
        new ObjectMapper().writeValue(response.getWriter(), loginResponse);

        // También añadimos el header Authorization
        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);
    }

    /**
     * Se ejecuta cuando la autenticación falla. Devuelve un JSON con un mensaje
     * genérico de error y la causa en el campo "error".
     *
     * @param request   petición HTTP
     * @param response  respuesta HTTP con código 401 y JSON de error
     * @param failed    excepción que indica la razón de la falla
     * @throws IOException      si ocurre un error al escribir en la respuesta
     * @throws ServletException si ocurre un error en el filtro HTTP
     */
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
