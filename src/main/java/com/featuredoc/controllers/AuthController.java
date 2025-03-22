package com.featuredoc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class AuthController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${base-url}")
    private String baseUrl;

    @Value("${cli-client-google-redirect}")
    private String cliRedirectURL;

    private final String scope = URLEncoder.encode("openid email profile", StandardCharsets.UTF_8);


    @GetMapping("/login")
    public ResponseEntity<Void> initiateLogin() {
        // Build the Google OAuth2 authorization URL
        String authorizationUrl = UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/auth")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", baseUrl + "/auth/token")
                .queryParam("response_type", "code")
                .queryParam("scope", "openid%20email%20profile")
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .build()
                .toUriString();

        // Redirect the user to the Google OAuth2 authorization page
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(authorizationUrl))
                .build();
    }


    @GetMapping("/auth/token")
    public ResponseEntity<Map<String, String>> getToken(
            @RequestParam("code") String code,
            @RequestParam(name = "channel", required = false) String channel
    ) throws Exception {

        String redirectURI = baseUrl+"/auth/token";

        if (Objects.equals(channel, "CLI")) {
            redirectURI = cliRedirectURL;
        }

        String accessToken = exchangeCodeForAccessToken(code, redirectURI);

        if (accessToken == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Collections.singletonMap("error", "Failed to exchange code for access token"));
        }


        Map<String, String> responseData = new HashMap<>();
        responseData.put("access_token", accessToken);
        return ResponseEntity.ok(responseData);
    }

    private String exchangeCodeForAccessToken(String code, String redirectURI) throws Exception {
        String url = "https://oauth2.googleapis.com/token";
        ObjectMapper objectMapper = new ObjectMapper();
        String body =
                "code=" + URLDecoder.decode(code, StandardCharsets.UTF_8) +
                        "&client_id=" + clientId +
                        "&client_secret=" + clientSecret +
                        "&redirect_uri=" + redirectURI +
                        "&grant_type=authorization_code" +
                        "&scope=openid%20email%20profile";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> resp = client.send(request, BodyHandlers.ofString());

        HashMap<String, String> map = objectMapper.readValue(resp.body(), new TypeReference<HashMap<String, String>>() {});

        return map.get("id_token");
    }

}