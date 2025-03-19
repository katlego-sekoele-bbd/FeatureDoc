package com.featuredoc.config;

import com.featuredoc.filters.JwtAuthenticationFilter;
import com.featuredoc.services.CustomOAuth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.featuredoc.services.UserService;
import com.featuredoc.helpers.Jwt;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.Customizer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
    private UserService userService;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    @Lazy
    Jwt jwt;

    @Autowired
    @Lazy
    private OAuth2AuthorizedClientService authorizedClientService;

    @Bean
    public SecretKey secretKey() {
        return Jwts.SIG.HS256.key().build(); // Generate a secure key
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests
                        (auth -> auth
                                .requestMatchers("/auth/token").permitAll()
                                .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Disable sessions
                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService()))
                        .successHandler(successHandler())
                        .failureHandler(failureHandler()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter
                .build();
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return customOAuth2UserService;
    }

    private AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {

            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                    token.getAuthorizedClientRegistrationId(),
                    token.getName());

            String email = (String) token.getPrincipal().getAttributes().get("email");

            String accessToken = client.getAccessToken().getTokenValue();

            String jwtToken = jwt.generateJwtToken(accessToken);

            Map<String, String> responseData = new HashMap<>();
            responseData.put("status", "success");
            responseData.put("message", email + " logged in successfully");
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
}

