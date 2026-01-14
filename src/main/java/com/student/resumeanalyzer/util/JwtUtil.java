package com.student.resumeanalyzer.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Small helper class for creating and validating JWT tokens
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret:change-me-secret}")
    private String secret;

    @Value("${jwt.expirationMs:86400000}") // 1 day by default
    private long expirationMs;

    /**
     * Create a simple JWT with user id and email
     */
    public String generateToken(String userId, String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withSubject(userId)
                .withClaim("email", email)
                .withIssuedAt(now)
                .withExpiresAt(expiry)
                .sign(algorithm);
    }

    /**
     * Validate token and return decoded JWT
     * Returns null if token is invalid
     */
    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm).build().verify(token);
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}

