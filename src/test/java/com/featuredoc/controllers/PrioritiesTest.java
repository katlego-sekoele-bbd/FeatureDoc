package com.featuredoc.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.featuredoc.exceptionhandlers.GlobalExceptionHandler;
import com.featuredoc.exceptions.ResourceNotFoundException;
import com.featuredoc.helpers.JsonWriter;
import com.featuredoc.models.Priority;
import com.featuredoc.models.Role;
import com.featuredoc.services.PriorityService;
import com.featuredoc.services.RoleService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;

@WebMvcTest(Priorities.class)
@ImportAutoConfiguration(GlobalExceptionHandler.class)
class PrioritiesTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PriorityService priorityService;

    private final List<Priority> mockPriorities = List.of(
            new Priority(1, "Low"),
            new Priority(2, "Medium"),
            new Priority(3, "High")
    );

    private final Priority mockCreatePriority = new Priority(4, "Critical");

    @Test
    @WithMockUser()
    void getAllPriorities() throws Exception {
        when(priorityService.getAllPriorities())
                .thenReturn(mockPriorities);

        this.mockMvc.perform(get("/priorities"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", isA(List.class)))
                .andExpect(jsonPath("$[0].priorityID", isA(Integer.class)))
                .andExpect(jsonPath("$[0].description", isA(String.class)));
    }

    @Test
    @WithMockUser()
    void createPriority() throws Exception {
        when(priorityService.createPriority(mockCreatePriority))
                .thenReturn(mockCreatePriority);

        JsonWriter<Priority> jsonWriter = new JsonWriter<>(mockCreatePriority);
        String roleJson = jsonWriter.toJsonString();

        Integer expectedCreatedRoleID = mockCreatePriority.getPriorityID();
        String expectedCreatedRoleName = mockCreatePriority.getDescription();

        this.mockMvc.perform(post("/priorities").with(csrf()).contentType("application/json").content(roleJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.priorityID", isA(Integer.class)))
                .andExpect(jsonPath("$.priorityID", is(expectedCreatedRoleID)))
                .andExpect(jsonPath("$.description", isA(String.class)))
                .andExpect(jsonPath("$.description", is(expectedCreatedRoleName)));
    }

    @Test
    @WithMockUser()
    void getPriorityById() throws Exception {

        final int validPriorityID = 1;
        final int invalidPriorityID = -1;
        final int nonExistentPriorityID = Integer.MAX_VALUE;

        when(priorityService.getPriorityById(validPriorityID))
                .thenReturn(Optional.ofNullable(mockPriorities.get(validPriorityID-1)));
        when(priorityService.getPriorityById(invalidPriorityID))
                .thenThrow(new ConstraintViolationException("PriorityID must be a positive integer", new HashSet<>()));
        when(priorityService.getPriorityById(nonExistentPriorityID))
                .thenThrow(new ResourceNotFoundException(String.format("Priority with priorityID=%s not found", nonExistentPriorityID)));

        this.mockMvc.perform(get("/priorities/" + validPriorityID))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.priorityID", isA(Integer.class)))
                .andExpect(jsonPath("$.priorityID", is(validPriorityID)))
                .andExpect(jsonPath("$.description", isA(String.class)));

        this.mockMvc.perform(get("/priorities/" + invalidPriorityID))
                .andExpect(status().is(400))
                .andExpect(result -> assertInstanceOf(ConstraintViolationException.class, result.getResolvedException()));

        this.mockMvc.perform(get("/priorities/" + nonExistentPriorityID))
                .andExpect(status().is(404))
                .andExpect(result -> assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @WithMockUser()
    void deletePriorityById() throws Exception {
        final int validPriorityID = 1;
        final int invalidPriorityID = -1;
        final int nonExistentPriorityID = Integer.MAX_VALUE;

        Mockito.doNothing().when(priorityService).deletePriorityById(validPriorityID);
        Mockito.doThrow(ConstraintViolationException.class).when(priorityService).deletePriorityById(invalidPriorityID);
        Mockito.doThrow(ResourceNotFoundException.class).when(priorityService).deletePriorityById(nonExistentPriorityID);

        this.mockMvc.perform(delete("/priorities/" + validPriorityID).with(csrf()))
                .andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(delete("/priorities/" + invalidPriorityID).with(csrf()))
                .andExpect(status().is(400))
                .andExpect(result -> assertInstanceOf(ConstraintViolationException.class, result.getResolvedException()));

        this.mockMvc.perform(delete("/priorities/" + nonExistentPriorityID).with(csrf()))
                .andExpect(status().is(404))
                .andExpect(result -> assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()));
    }
}