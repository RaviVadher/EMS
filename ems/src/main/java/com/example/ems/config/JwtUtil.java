package com.example.ems.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {

    private static final String SECRET = "vadherraviVadherRavivadherravivadherraviVadherRavivadherravivadherraviVadherRavivadherravi";
    //private static final SecretKey SECRET_KEY = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS512.getJcaName());
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder()
            .decode(Base64.getEncoder()
                    .encodeToString(SECRET.getBytes())));

    long EXPIRATION_TIME = 28800000;

    // Generate JWT token
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("exp", new Date(System.currentTimeMillis() + EXPIRATION_TIME));

        return Jwts.builder()
                .claims(claims)
                .signWith(SECRET_KEY)
                .compact();
    }

    // Parse JWT to extract claims (using Jws<Claims>)
    public Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token);
    }

    // Extract username from JWT
    public String extractUsername(String token) {
        return parseToken(token).getPayload().getSubject();
    }

    // Check if the token is expired
    public boolean isTokenExpired(String token) {
        return parseToken(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    // Validate the token
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }
}


