package com.featuredoc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.featuredoc.services.FeatureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {AuthController.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/feature";

    private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor mockJwt = jwt().jwt(jwt ->
                    jwt.subject("test@test.com")
                            .claim("email", "test@example.com")
//                    .claim("scope", "ROLE_CAN_DELETE")
    );


    @Test
    void initiateLoginBestCase() throws Exception {

        // Expected authorization URL
        String expectedUrl = "https://accounts.google.com/o/oauth2/auth" +
                "?client_id=your-client-id" +
                "&redirect_uri=http://localhost:8080/auth/token" +
                "&response_type=code" +
                "&scope=openid%20email%20profile" +
                "&access_type=offline" +
                "&prompt=consent";

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML));
    }

    @Test
    void getToken() {
    }
}