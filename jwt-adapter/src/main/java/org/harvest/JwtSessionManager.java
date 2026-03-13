package org.harvest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.harvest.application.dto.value.Role;
import org.harvest.application.dto.value.SessionCreationRequest;
import org.harvest.application.port.outbound.security.SessionManager;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.shared.exception.AuthenticationException;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JwtSessionManager implements SessionManager {
    private final JwtConfiguration config;
    private final SecretKey key;

    /**
     * Creates a new JwtSessionManager with the given configuration.
     *
     * @param config the JWT configuration
     */
    public JwtSessionManager(JwtConfiguration config) {
        if (config == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }
        config.validate();
        this.config = config;
        this.key = Keys.hmacShaKeyFor(config.signingKey().getBytes());
    }
    @Override
    public String createSession(SessionCreationRequest request) {
        return Jwts.builder()
                .subject(request.userId().toString()) // id user
                .claim("organizationId", request.organizationId().toString()) // id organisasi
                .claim("role", request.role().name()) // role user tersebut
                .issuedAt(new Date()) //waktu toekn dibuat
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // waktu kadaluarsa token
                .signWith(key) // menggunakan secret key
                .compact();// ini unutk membuat token

    }
    private boolean isTokenExpired(Claims claims) {
        Instant now = config.clock().instant();
        return claims.getExpiration().toInstant().isBefore(now);
    }


    @Override
    public void invalidateAllSessions(UUID uuid) {
//
    }

    @Override
    public UserSession getSession(String token) {

        try {

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();


            if (isTokenExpired(claims)) {
                throw new AuthenticationException("Token has expired");
            }

            UUID userId = UUID.fromString(claims.getSubject());

            UUID organizationId = UUID.fromString(
                    claims.get("organizationId", String.class)
            );

            Role role = Role.valueOf(
                    claims.get("role", String.class)
            );

            SessionPayload sessionPayload =
                    new SessionPayload(userId, organizationId, role);
            return new SessionUsers(sessionPayload);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new AuthenticationException("JWT token sudah expired");
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            throw new AuthenticationException("JWT token format tidak valid");
        } catch (io.jsonwebtoken.security.SecurityException e) {
            throw new AuthenticationException("JWT signature tidak valid");
        } catch (io.jsonwebtoken.JwtException e) {
            throw new AuthenticationException("JWT token tidak valid");
        } catch (Exception e) {
            throw new AuthenticationException("Terjadi kesalahan saat membaca session",e);
        }
    }

    @Override
    public Instant now() {
        return Instant.now();
    }

}
