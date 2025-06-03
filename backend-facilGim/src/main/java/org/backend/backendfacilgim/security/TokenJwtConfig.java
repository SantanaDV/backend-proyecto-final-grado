package org.backend.backendfacilgim.security;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

/**
 * Configuración estática de JWT para la aplicación.
 * <p>
 * Incluye la clave secreta para firmar y validar los tokens, así como constantes
 * relacionadas con el encabezado HTTP y el prefijo del token.
 * </p>
 *
 * @author Francisco Santana
 */
public class TokenJwtConfig {

    /**
     * Clave secreta utilizada para firmar y validar los tokens JWT.
     * <p>
     * Se genera con el algoritmo HS256 de la librería jjwt.
     * </p>
     */
    // Generamos una clave secreta para firmar el token
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    /**
     * Prefijo estándar para los tokens en el header Authorization.
     * <p>
     * Ejemplo: "Authorization: Bearer &lt;token&gt;".</p>
     */
    public static final String PREFIX_TOKEN = "Bearer ";

    /**
     * Nombre de la cabecera HTTP donde se incluye el token JWT.
     */
    public static final String HEADER_AUTHORIZATION = "Authorization";

    /**
     * Tipo de contenido usado en las respuestas JSON del sistema de autenticación.
     */
    public static final String CONTENT_TYPE = "application/json";
}
