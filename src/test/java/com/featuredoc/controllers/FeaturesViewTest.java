package com.featuredoc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.featuredoc.exceptions.ResourceNotFoundException;
import com.featuredoc.models.FeatureView;
import com.featuredoc.services.FeatureViewService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {FeaturesView.class})
class FeaturesViewTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FeatureViewService featureViewService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/feature-versions";

    private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor mockJwt = jwt().jwt(jwt ->
                    jwt.subject("test@test.com")
                            .claim("email", "test@example.com")
    );

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSxxx")
            .withZone(ZoneOffset.UTC);

    @Test
    public void getLatestFeatureVersionBestCase() throws Exception {
        when(featureViewService.getLatestFeatureVersionByFeatureId(any(Integer.class)))
                .thenReturn(Optional.of(new FeatureView()));

        mockMvc.perform(get(BASE_URL + "/" + 1)
                        .with(mockJwt))
                .andExpect(status().is(200));

        verify(featureViewService, times(1)).getLatestFeatureVersionByFeatureId(any());
    }

    @Test
    public void getLatestFeatureVersionFeatureIDNotExists() throws Exception {
        when(featureViewService.getLatestFeatureVersionByFeatureId(any(Integer.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/" + Integer.MAX_VALUE)
                        .with(mockJwt))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").isEmpty());

        verify(featureViewService, times(1)).getLatestFeatureVersionByFeatureId(any());
    }

    @Test
    public void getLatestFeatureVersionBadID() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + Integer.MIN_VALUE)
                        .with(mockJwt))
                .andExpect(status().is(400));

        verify(featureViewService, times(0)).getLatestFeatureVersionByFeatureId(any());
    }

    @Test
    public void getAllFeatureVersionsBestCase() throws Exception {
        when(featureViewService.getAllVersionsByFeatureId(any()))
                .thenReturn(List.of(
                        new FeatureView(),
                        new FeatureView()
                ));

        mockMvc.perform(get(BASE_URL + "/1/history")
                        .with(mockJwt))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray());

        verify(featureViewService, times(1)).getAllVersionsByFeatureId(any());
    }

    @Test
    public void getAllFeatureVersionsNoHistory() throws Exception {
        when(featureViewService.getAllVersionsByFeatureId(any()))
                .thenReturn(List.of());

        mockMvc.perform(get(BASE_URL + "/1/history")
                        .with(mockJwt))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(jsonPath("$").isArray());

        verify(featureViewService, times(1)).getAllVersionsByFeatureId(any());
    }

    @Test
    public void getAllFeatureVersionsBadID() throws Exception {
        mockMvc.perform(get(BASE_URL + "/1/history")
                        .with(mockJwt))
                .andExpect(status().is(400));

        verify(featureViewService, times(0)).getAllVersionsByFeatureId(any());
    }

}