package com.featuredoc.helpers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class Jwt {

    @Autowired
    @Lazy
    SecretKey secretKey;

    public String generateJwtToken(String accessToken, String email, Collection<? extends GrantedAuthority> authorities) {
        // Create the JWT claims

        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .claim("access_token", accessToken)
                .claim("email", email) // Include email
                .claim("roles", roles)// You can include the OAuth2 access token as a claim
                .issuedAt(new Date()) // Set issue time
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // Set expiration (1 hour from now)
                .signWith(secretKey)
                .compact(); // Create the JWT token string
    }

    public Jws<Claims> verifyJwtToken(String jwtToken) {
        try {

            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwtToken);
        } catch (SignatureException e) {

            throw new IllegalArgumentException("Invalid JWT signature", e);
        } catch (Exception e) {

            throw new IllegalArgumentException("Invalid or expired JWT token", e);
        }
    }
}
