package com.featuredoc.helpers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class Jwt {

    @Autowired
    @Lazy
    SecretKey secretKey;

    public String generateJwtToken(String accessToken) {
        // Create the JWT claims
        return Jwts.builder()
                .claim("access_token", accessToken) // You can include the OAuth2 access token as a claim
                .issuedAt(new Date()) // Set issue time
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // Set expiration (1 hour from now)
                .signWith(secretKey)
                .compact(); // Create the JWT token string
    }

    public Jws<Claims> verifyJwtToken(String jwtToken) {
        try {
            // Parse and verify the token
            return Jwts.parser()
                    .verifyWith(secretKey) // Use the same key for verification
                    .build()
                    .parseSignedClaims(jwtToken); // Parse the token and validate its signature
        } catch (SignatureException e) {
            // Handle invalid signature
            throw new IllegalArgumentException("Invalid JWT signature", e);
        } catch (Exception e) {
            // Handle other exceptions (e.g., expired token, malformed token)
            throw new IllegalArgumentException("Invalid or expired JWT token", e);
        }
    }
}
