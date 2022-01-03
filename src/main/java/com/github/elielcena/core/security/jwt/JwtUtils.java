package com.github.elielcena.core.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.github.elielcena.core.security.service.UserDetailsImpl;
import com.github.elielcena.domain.exception.BusinessException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author elielcena
 *
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${app.auth.jwtSecret}")
    private String jwtSecret;

    @Value("${app.auth.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            String message = "Invalid JWT signature: %s";
            log.error(String.format(message, e.getMessage()));
            throw new BusinessException(String.format(message, e.getMessage()));
        } catch (MalformedJwtException e) {
            String message = "Invalid JWT token: %s";
            log.error(String.format(message, e.getMessage()));
            throw new BusinessException(String.format(message, e.getMessage()));
        } catch (ExpiredJwtException e) {
            String message = "JWT token is expired: %s";
            log.error(String.format(message, e.getMessage()));
            throw new BusinessException(String.format(message, e.getMessage()));
        } catch (UnsupportedJwtException e) {
            String message = "JWT token is unsupported: %s";
            log.error(String.format(message, e.getMessage()));
            throw new BusinessException(String.format(message, e.getMessage()));
        } catch (IllegalArgumentException e) {
            String message = "JWT claims string is empty: %s";
            log.error(String.format(message, e.getMessage()));
            throw new BusinessException(String.format(message, e.getMessage()));
        }
    }
}
