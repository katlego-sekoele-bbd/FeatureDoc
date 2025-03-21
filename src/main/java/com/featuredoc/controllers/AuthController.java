package com.featuredoc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;

@RestController
public class AuthController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @GetMapping("/auth/token")
    public ResponseEntity<Map<String, String>> getToken(
            @RequestParam("code") String code) throws Exception {
        String accessToken = exchangeCodeForAccessToken(code);

        if (accessToken == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Collections.singletonMap("error", "Failed to exchange code for access token"));
        }


        Map<String, String> responseData = new HashMap<>();
        responseData.put("access_token", accessToken);
        return ResponseEntity.ok(responseData);
    }

    private String exchangeCodeForAccessToken(String code) throws Exception {
        String url = "https://oauth2.googleapis.com/token";
        ObjectMapper objectMapper = new ObjectMapper();
        String body =
                "code=" + URLDecoder.decode(code, StandardCharsets.UTF_8) +
                        "&client_id=" + clientId +
                        "&client_secret=" + clientSecret +
                        "&redirect_uri=" + "http://localhost:3000/login/oauth2/code/google" +
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

//    private OAuth2User loadUser(String accessToken) {
//        // Create an OAuth2UserRequest with the access token
//        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
//        OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, new OAuth2AccessToken(
//                OAuth2AccessToken.TokenType.BEARER,
//                accessToken,
//                null,
//                null
//        ));
//
//        // Load the user details using the custom OAuth2UserService
//        return customOAuth2UserService.loadUser(userRequest);
//    }

}