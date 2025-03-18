package com.featuredoc.config;

import com.featuredoc.services.CustomOAuth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    public CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    @Lazy
    private OAuth2AuthorizedClientService authorizedClientService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests
                        (auth -> auth.anyRequest().authenticated())
                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService()))
                        .successHandler(successHandler())
                        .failureHandler(failureHandler()))
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }

    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId("8421127185-l0d2bmeb37ri8f7qohh3miot46hgj6l4.apps.googleusercontent.com")
                .clientSecret("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")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "email", "address", "phone")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .build();
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return customOAuth2UserService;
    }

    private AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {

            String name = authentication.getName();

            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                    token.getAuthorizedClientRegistrationId(),
                    token.getName());

            String accessToken = client.getAccessToken().getTokenValue();

            String jwtToken = generateJwtToken(accessToken);

            Map<String, String> responseData = new HashMap<>();
            responseData.put("status", "success");
            responseData.put("message", name + " logged in successfully");
            responseData.put("access_token", jwtToken);  // Include the JWT token in the response

            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            response.setContentType("application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                objectMapper.writeValue(response.getWriter(), responseData);
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception properly in production code
            }


        };
    }

    private AuthenticationFailureHandler failureHandler() {

        return (request, response, authentication) -> {
            Map<String, String> responseData = new HashMap<>();
            responseData.put("status", "error");
            responseData.put("message", "Login failed");
            responseData.put("error", authentication.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized

            response.setContentType("application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                objectMapper.writeValue(response.getWriter(), responseData);
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception properly in production code
            }
        };
    }

    public String generateJwtToken(String accessToken) {

        SecretKey key = Jwts.SIG.HS256.key().build();

        // Create the JWT claims
        return Jwts.builder()
                .claim("access_token", accessToken) // You can include the OAuth2 access token as a claim
                .issuedAt(new Date()) // Set issue time
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // Set expiration (1 hour from now)
                .signWith(key)
                .compact(); // Create the JWT token string
    }

    public Jws<Claims> verifyJwtToken(String jwtToken) {
        try {
            // Parse the token and validate its signature
            return Jwts.parser()
                    .verifyWith(Jwts.SIG.HS256.key().build())
                    .build().parseSignedClaims(jwtToken);
        } catch (SignatureException e) {
            throw new IllegalArgumentException("Invalid or expired JWT token");
        }
    }
}

