package com.wilson.api_meteorologica.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
/**
 * Clase para la gestión de tokens JWT en la API.
 * Se encarga de generar, validar y extraer información de los tokens JWT.
 */
@Component
public class JwtUtils {
    /**
     * Clave secreta utilizada para firmar los tokens JWT.
     * Se obtiene desde la configuración de la aplicación (application.properties).
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Tiempo de expiración del token JWT en milisegundos.
     */
    @Value("${jwt.expiration}")
    private int jwtExpiration;

    /**
     * Obtiene la clave de firma para JWT utilizando HMAC-SHA512.
     * La clave no se decodifica en Base64 por seguridad.
     * @return SecretKey para firmar y validar JWT.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)); // NO DECODIFICAR BASE64
    }

    /**
     * Genera un token JWT basado en la autenticación del usuario.
     * @param authentication Objeto de autenticación del usuario.
     * @return Token JWT generado.
     */
    public String generateJwtToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // Firma con clave segura
                .compact();
    }

    /**
     * Extrae el nombre de usuario desde un token JWT válido.
     * @param token Token JWT.
     * @return Nombre de usuario contenido en el token.
     */
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Valida si un token JWT es válido y no ha sido alterado.
     * @param authToken Token JWT recibido.
     * @return true si el token es válido, false si es inválido o expiró.
     */
   public boolean validateJwtToken(String authToken) {
       try {
           Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
           return true;
       } catch (JwtException | IllegalArgumentException e) {
           System.err.println("Token inválido: " + e.getMessage());
           return false;
       }
    }
}
