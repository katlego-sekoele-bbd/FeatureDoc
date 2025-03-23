package com.featuredoc.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.featuredoc.dto.FeatureRequest;
import com.featuredoc.exceptions.ResourceNotFoundException;
import com.featuredoc.models.Feature;
import com.featuredoc.models.FeatureVersion;
import com.featuredoc.services.FeatureService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {FeatureController.class})
class FeatureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FeatureService featureService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/feature";

    private static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor mockJwt = jwt().jwt(jwt ->
            jwt.subject("test@test.com")
                    .claim("email", "test@example.com")
//                    .claim("scope", "ROLE_CAN_DELETE")
    );

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSxxx")
            .withZone(ZoneOffset.UTC);

    @Test
    public void addFeatureBestCase() throws Exception {
        FeatureRequest featureRequest = new FeatureRequest(1L, 1L, 1L, 1, 1, 1L, "name", "description", "url");
        Feature feature = new Feature(1L, 1L, Timestamp.from(Instant.now()));

        when(this.featureService.addFeature(any(FeatureRequest.class))).thenReturn(feature);

        mockMvc.perform(
                post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(featureRequest))
                        .with(mockJwt)
                        .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.featureID").value(feature.getFeatureID()))
                .andExpect(jsonPath("$.createdBy").value(feature.getCreatedBy()))
                .andExpect(jsonPath("$.createdAt").value(formatter.format(feature.getCreatedAt().toInstant())));

        verify(featureService, times(1)).addFeature(any(FeatureRequest.class));

    }

    @Test
    public void addFeatureUserNotAuthenticated() throws Exception {
        FeatureRequest featureRequest = new FeatureRequest(1L, 1L, 1L, 1, 1, 1L, "name", "description", "url");
        Feature feature = new Feature(1L, 1L, Timestamp.from(Instant.now()));

        when(this.featureService.addFeature(any(FeatureRequest.class))).thenReturn(feature);

        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(featureRequest)))
                .andExpect(status().is(403));

        verify(featureService, times(0)).addFeature(any(FeatureRequest.class));

    }

    @Test
    public void addFeatureInvalidFeatureRequest() throws Exception {
        FeatureRequest featureRequest = new FeatureRequest(1L, 1L, 1L, null, null, 1L, "name", "description", "url");
        Feature feature = new Feature(1L, 1L, Timestamp.from(Instant.now()));
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        when(this.featureService.addFeature(any(FeatureRequest.class))).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(featureRequest))
                                .with(mockJwt)
                                .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().is(400));

        verify(featureService, times(1)).addFeature(any(FeatureRequest.class));

    }

    @Test
    public void addFeatureFeatureWithSameNameExists() throws Exception {
        FeatureRequest featureRequest = new FeatureRequest(1L, 1L, 1L, 1, 1, 1L, "name", "description", "url");
        Feature feature = new Feature(1L, 1L, Timestamp.from(Instant.now()));
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        when(this.featureService.addFeature(any(FeatureRequest.class))).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(featureRequest))
                                .with(mockJwt)
                                .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().is(409));

        verify(featureService, times(1)).addFeature(any(FeatureRequest.class));

    }

    @Test
    public void updateFeatureBestCase() throws Exception {
        FeatureRequest featureRequest = new FeatureRequest(1L, 1L, 1L, 1, 1, 1L, "name", "description", "url");
        Feature feature = new Feature(1L, 1L, Timestamp.from(Instant.now()));
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        when(this.featureService.updateFeature(any(FeatureRequest.class))).thenReturn(new FeatureVersion());

        mockMvc.perform(
                        put(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(featureRequest))
                                .with(mockJwt)
                                .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().is(200));

        verify(featureService, times(1)).updateFeature(any(FeatureRequest.class));

    }

    @Test
    public void updateFeatureUserNotAuthenticated() throws Exception {
        FeatureRequest featureRequest = new FeatureRequest(1L, 1L, 1L, 1, 1, 1L, "name", "description", "url");
        Feature feature = new Feature(1L, 1L, Timestamp.from(Instant.now()));

        when(this.featureService.addFeature(any(FeatureRequest.class))).thenReturn(feature);

        mockMvc.perform(
                        put(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(featureRequest)))
                .andExpect(status().is(403));

        verify(featureService, times(0)).addFeature(any(FeatureRequest.class));

    }

    @Test
    public void updateFeatureInvalidFeatureRequest() throws Exception {
        FeatureRequest featureRequest = new FeatureRequest(1L, null, 1L, 1, 1, 1L, "name", "description", "url");
        Feature feature = new Feature(1L, 1L, Timestamp.from(Instant.now()));
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        when(this.featureService.updateFeature(any(FeatureRequest.class))).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(
                        put(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(featureRequest))
                                .with(mockJwt)
                                .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().is(400));

        verify(featureService, times(1)).updateFeature(any(FeatureRequest.class));

    }

    @Test
    public void updateFeatureFeatureIDNotExists() throws Exception {
        FeatureRequest featureRequest = new FeatureRequest(1L, null, 1L, 1, 1, 1L, "name", "description", "url");
        Feature feature = new Feature(1L, 1L, Timestamp.from(Instant.now()));
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        when(this.featureService.updateFeature(any(FeatureRequest.class))).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(
                        put(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(featureRequest))
                                .with(mockJwt)
                                .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().is(404));

        verify(featureService, times(1)).updateFeature(any(FeatureRequest.class));

    }

}