package com.featuredoc.controllers;

import com.featuredoc.exceptionhandlers.GlobalExceptionHandler;
import com.featuredoc.exceptions.ResourceNotFoundException;
import com.featuredoc.models.FeatureView;
import com.featuredoc.services.FeatureViewService;
import com.featuredoc.services.RoleService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FeaturesView.class)
@ImportAutoConfiguration(GlobalExceptionHandler.class)
class FeaturesViewTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FeatureViewService featureViewService;

    private final List<FeatureView> mockFeatures = List.of(
            new FeatureView(1,
                "feature 1",
                "Person 1",
                LocalDateTime.now(),
                1,
                "Person 1",
                "In Progress",
                "High",
                "Person 1",
                "Feature",
                "Description",
                "url",
                LocalDateTime.now(),
                null
            ),
            new FeatureView(2,
                "feature 2",
                "Person 2",
                LocalDateTime.now(),
                2,
                "Person 2",
                "In Progress",
                "High",
                "Person 2",
                "Feature",
                "Description",
                "url",
                LocalDateTime.now(),
                null
            ),
            new FeatureView(3,
                "feature 3",
                "Person 3",
                LocalDateTime.now(),
                3,
                "Person 3",
                "In Progress",
                "High",
                "Person 3",
                "Feature",
                "Description",
                "url",
                LocalDateTime.now(),
                null
            )
    );

    @Test
    @WithMockUser()
    void getLatestFeatureVersion() throws Exception {

        int validId = 1;
        int invalidId = -1;
        int nullId = Integer.MAX_VALUE;

        when(featureViewService.getLatestFeatureVersionByFeatureId(validId))
                .thenReturn(Optional.ofNullable(mockFeatures.get(validId - 1)));

        when(featureViewService.getLatestFeatureVersionByFeatureId(invalidId))
                .thenThrow(ConstraintViolationException.class);

        when(featureViewService.getLatestFeatureVersionByFeatureId(nullId))
                .thenThrow(ResourceNotFoundException.class);

        this.mockMvc.perform(get("/feature-versions/" + validId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray());

        this.mockMvc.perform(get("/feature-versions/" + invalidId))
                .andExpect(status().is(400))
                .andExpect(result -> assertInstanceOf(ConstraintViolationException.class, result.getResolvedException()));

        this.mockMvc.perform(get("/feature-versions/" + nullId))
                .andExpect(status().is(404))
                .andExpect(result -> assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @WithMockUser
    void getAllFeatureVersions() throws Exception {
        int validId = 1;
        int invalidId = -1;
        int nullId = Integer.MAX_VALUE;

        when(featureViewService.getAllVersionsByFeatureId(validId))
                .thenReturn(mockFeatures);

        when(featureViewService.getAllVersionsByFeatureId(invalidId))
                .thenThrow(ConstraintViolationException.class);

        when(featureViewService.getAllVersionsByFeatureId(nullId))
                .thenThrow(ResourceNotFoundException.class);

        this.mockMvc.perform(get("/feature-versions/" + validId + "/history"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray());

        this.mockMvc.perform(get("/feature-versions/" + invalidId + "/history"))
                .andExpect(status().is(400))
                .andExpect(result -> assertInstanceOf(ConstraintViolationException.class, result.getResolvedException()));

        this.mockMvc.perform(get("/feature-versions/" + nullId + "/history"))
                .andExpect(status().is(404))
                .andExpect(result -> assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()));
    }
}