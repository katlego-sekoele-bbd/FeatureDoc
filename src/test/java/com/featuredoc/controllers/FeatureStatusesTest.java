package com.featuredoc.controllers;

import com.featuredoc.exceptionhandlers.GlobalExceptionHandler;
import com.featuredoc.exceptions.ResourceNotFoundException;
import com.featuredoc.helpers.JsonWriter;
import com.featuredoc.models.FeatureStatus;
import com.featuredoc.models.Role;
import com.featuredoc.services.FeatureStatusService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FeatureStatuses.class)
@ImportAutoConfiguration(GlobalExceptionHandler.class)
class FeatureStatusesTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FeatureStatusService featureStatusService;

    private final List<FeatureStatus> mockFeatureStatuses = List.of(
            new FeatureStatus(1, "FeatureStatus 1"),
            new FeatureStatus(2, "FeatureStatus 2"),
            new FeatureStatus(3, "FeatureStatus 3"),
            new FeatureStatus(4, "FeatureStatus 4"),
            new FeatureStatus(5, "FeatureStatus 5")
    );

    private final FeatureStatus mockCreateFeatureStatus = new FeatureStatus(6, "FeatureStatus 6");


    @Test
    void getAllFeatureStatuses() throws Exception {
        when(featureStatusService.getAllFeatureStatuses())
                .thenReturn(mockFeatureStatuses);

        this.mockMvc.perform(get("/feature-statuses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", isA(List.class)))
                .andExpect(jsonPath("$[0].featureStatusID", isA(Integer.class)))
                .andExpect(jsonPath("$[0].description", isA(String.class)));
    }

    @Test
    void createFeatureStatus() throws Exception {
        when(featureStatusService.createFeatureStatus(mockCreateFeatureStatus))
                .thenReturn(mockCreateFeatureStatus);

        JsonWriter<FeatureStatus> jsonWriter = new JsonWriter<>(mockCreateFeatureStatus);
        String featureStatusJson = jsonWriter.toJsonString();

        Integer expectedCreatedFeatureStatusID = mockCreateFeatureStatus.getFeatureStatusID();
        String expectedCreatedFeatureStatusDescription = mockCreateFeatureStatus.getDescription();

        this.mockMvc.perform(post("/feature-statuses").contentType("application/json").content(featureStatusJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.featureStatusID", isA(Integer.class)))
                .andExpect(jsonPath("$.featureStatusID", is(expectedCreatedFeatureStatusID)))
                .andExpect(jsonPath("$.description", isA(String.class)))
                .andExpect(jsonPath("$.description", is(expectedCreatedFeatureStatusDescription)));
    }

    @Test
    void getFeatureStatusByID() throws Exception {
        final int validFeatureStatusID = 1;
        final int invalidFeatureStatusID = -1;
        final int nonExistentFeatureStatusID = Integer.MAX_VALUE;

        when(featureStatusService.getFeatureStatusById(validFeatureStatusID))
                .thenReturn(Optional.ofNullable(mockFeatureStatuses.get(validFeatureStatusID-1)));
        when(featureStatusService.getFeatureStatusById(invalidFeatureStatusID))
                .thenThrow(new ConstraintViolationException("FeatureStatusID must be a positive integer", new HashSet<>()));
        when(featureStatusService.getFeatureStatusById(nonExistentFeatureStatusID))
                .thenThrow(new ResourceNotFoundException(String.format("FeatureStatus with featureStatusID=%s not found", nonExistentFeatureStatusID)));

        this.mockMvc.perform(get("/feature-statuses/" + validFeatureStatusID))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.featureStatusID", isA(Integer.class)))
                .andExpect(jsonPath("$.featureStatusID", is(validFeatureStatusID)))
                .andExpect(jsonPath("$.description", isA(String.class)));

        this.mockMvc.perform(get("/feature-statuses/" + invalidFeatureStatusID))
                .andExpect(status().is(400))
                .andExpect(result -> assertInstanceOf(ConstraintViolationException.class, result.getResolvedException()));

        this.mockMvc.perform(get("/feature-statuses/" + nonExistentFeatureStatusID))
                .andExpect(status().is(404))
                .andExpect(result -> assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()));
    }

    @Test
    void deleteFeatureStatusByID() throws Exception {
        final int validFeatureStatusID = 1;
        final int invalidFeatureStatusID = -1;
        final int nonExistentFeatureStatusID = Integer.MAX_VALUE;

        Mockito.doNothing().when(featureStatusService).deleteFeatureStatusById(validFeatureStatusID);
        Mockito.doThrow(ConstraintViolationException.class).when(featureStatusService).deleteFeatureStatusById(invalidFeatureStatusID);
        Mockito.doThrow(ResourceNotFoundException.class).when(featureStatusService).deleteFeatureStatusById(nonExistentFeatureStatusID);

        this.mockMvc.perform(delete("/feature-statuses/" + validFeatureStatusID))
                .andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(delete("/feature-statuses/" + invalidFeatureStatusID))
                .andExpect(status().is(400))
                .andExpect(result -> assertInstanceOf(ConstraintViolationException.class, result.getResolvedException()));

        this.mockMvc.perform(delete("/feature-statuses/" + nonExistentFeatureStatusID))
                .andExpect(status().is(404))
                .andExpect(result -> assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()));
    }
}