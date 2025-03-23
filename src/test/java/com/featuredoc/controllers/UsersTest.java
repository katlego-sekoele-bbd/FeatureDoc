package com.featuredoc.controllers;

import com.featuredoc.models.User;
import com.featuredoc.services.EmailService;
import com.featuredoc.services.UserService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.featuredoc.models.FeatureView;
import com.featuredoc.services.FeatureViewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = {Users.class})
class UsersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private EmailService emailService;

    private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor mockJwt = jwt().jwt(jwt ->
            jwt.subject("test@test.com")
                    .claim("email", "test@example.com")
    );

    private static final String BASE_URL = "/users";

    @Test
    public void getAllUsersBestCase() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(List.of(
                        new User(),
                        new User(),
                        new User()
                ));

        mockMvc.perform(get(BASE_URL)
                .with(mockJwt))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());

        verify(userService, times(1)).getAllUsers();

    }

    @Test
    public void getAllUsersNoUsers() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(List.of());

        mockMvc.perform(get(BASE_URL)
                        .with(mockJwt))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void getUserByIdBestCase() throws Exception {
        when(userService.getUserById(any()))
                .thenReturn(Optional.of(new User("test", "test@test.com")));

        mockMvc.perform(get(BASE_URL + "/1")
                        .with(mockJwt))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").isNotEmpty());

        verify(userService, times(1)).getUserById(any());
    }

    @Test
    public void getUserByIdUserNotExist() throws Exception {
        when(userService.getUserById(any()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/1")
                        .with(mockJwt))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").isEmpty());

        verify(userService, times(1)).getUserById(any());
    }

    @Test
    public void getUserByIdBadID() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + Integer.MIN_VALUE)
                        .with(mockJwt))
                .andExpect(status().is(400));

        verify(userService, times(0)).getUserById(any());
    }

}