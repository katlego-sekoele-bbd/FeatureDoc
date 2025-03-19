package com.featuredoc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.featuredoc.models.CustomOAuth2User;
import com.featuredoc.services.CustomOAuth2UserService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    SecretKey secretKey;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @GetMapping("/auth/token")
    public void getToken(
            @RequestParam("code") String code,
            HttpServletResponse response) throws IOException {

        // Step 1: Exchange the authorization code for an access token
        String accessToken = exchangeCodeForAccessToken(code);

        if (accessToken == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Failed to exchange code for access token");
            return;
        }

        // Step 2: Load the user details using the access token
        OAuth2User oauth2User = loadUser(accessToken);

        if (oauth2User == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Failed to load user details");
            return;
        }

        // Step 3: Generate a JWT token
        String jwtToken = generateJwtToken(accessToken);
        // Step 4: Return the JWT token to the client
        Map<String, String> responseData = new HashMap<>();
        responseData.put("access_token", jwtToken);
        responseData.put("token_type", "Bearer");
        responseData.put("expires_in", "3600"); // Example: 1 hour

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), responseData);
    }

    private String exchangeCodeForAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare the request body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", "http://localhost:3000/login/oauth2/code/google");
        requestBody.add("grant_type", "authorization_code");

        // Prepare the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Send the request to Google's token endpoint
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token",
                request,
                Map.class
        );


        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        }

        return null;
    }

    private OAuth2User loadUser(String accessToken) {
        // Create an OAuth2UserRequest with the access token
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
        OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                accessToken,
                null,
                null
        ));

        // Load the user details using the custom OAuth2UserService
        return customOAuth2UserService.loadUser(userRequest);
    }

    private String generateJwtToken(String accessToken) {
        // Generate a JWT token (use your existing implementation)
        return Jwts.builder()
                .claim("access_token", accessToken)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(secretKey) // Use your secret key
                .compact();
    }
}