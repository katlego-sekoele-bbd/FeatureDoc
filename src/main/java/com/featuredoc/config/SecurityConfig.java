package com.featuredoc.config;

import com.featuredoc.models.CustomOAuth2User;
import com.featuredoc.services.CustomOAuth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    public CustomOAuth2UserService customOAuth2UserService;

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

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return customOAuth2UserService;
    }

    private AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {

            String name = authentication.getName();

            Map<String, String> responseData = new HashMap<>();
            responseData.put("status", "success");
            responseData.put("message", name + " logged in successfully");

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

