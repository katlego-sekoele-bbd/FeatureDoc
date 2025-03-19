package com.featuredoc.controllers;

import com.featuredoc.exceptionhandlers.GlobalExceptionHandler;
import com.featuredoc.exceptions.ResourceNotFoundException;
import com.featuredoc.helpers.JsonWriter;
import com.featuredoc.models.Role;
import static org.hamcrest.Matchers.*;

import com.featuredoc.services.RoleService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(Roles.class)
@ImportAutoConfiguration(GlobalExceptionHandler.class)
class RolesTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleService roleService;

    private final List<Role> mockRoles = List.of(
            new Role(1, "Role 1"),
            new Role(2,"Role 2"),
            new Role(3,"Role 3"),
            new Role(4,"Role 4"),
            new Role(5,"Role 5"),
            new Role(6,"Role 6"),
            new Role(7,"Role 7"),
            new Role(8,"Role 8"),
            new Role(9,"Role 9"),
            new Role(10,"Role 10")
        );

    private final Role mockCreateRole = new Role(11, "Role 11");

    @Test
    @WithMockUser()
    void getAllRoles() throws Exception {
        when(roleService.getAllRoles())
                .thenReturn(mockRoles);

        this.mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", isA(List.class)))
                .andExpect(jsonPath("$[0].roleID", isA(Integer.class)))
                .andExpect(jsonPath("$[0].roleName", isA(String.class)));
    }

    @Test
    @WithMockUser()
    void createRole() throws Exception {
        when(roleService.createRole(mockCreateRole))
                .thenReturn(mockCreateRole);

        JsonWriter<Role> jsonWriter = new JsonWriter<>(mockCreateRole);
        String roleJson = jsonWriter.toJsonString();

        Integer expectedCreatedRoleID = mockCreateRole.getRoleID();
        String expectedCreatedRoleName = mockCreateRole.getRoleName();

        this.mockMvc.perform(post("/roles").with(csrf()).contentType("application/json").content(roleJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.roleID", isA(Integer.class)))
                .andExpect(jsonPath("$.roleID", is(expectedCreatedRoleID)))
                .andExpect(jsonPath("$.roleName", isA(String.class)))
                .andExpect(jsonPath("$.roleName", is(expectedCreatedRoleName)));
    }

    @Test
    @WithMockUser()
    void getRoleByID() throws Exception {

        final int validRoleID = 1;
        final int invalidRoleID = -1;
        final int nonExistantRoleID = Integer.MAX_VALUE;

        when(roleService.getRoleById(validRoleID))
                .thenReturn(Optional.ofNullable(mockRoles.get(validRoleID-1)));
        when(roleService.getRoleById(invalidRoleID))
                .thenThrow(new ConstraintViolationException("RoleID must be a positive integer", new HashSet<>()));
        when(roleService.getRoleById(nonExistantRoleID))
                .thenThrow(new ResourceNotFoundException(String.format("Role with roleId=%s not found", nonExistantRoleID)));

        this.mockMvc.perform(get("/roles/" + validRoleID))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.roleID", isA(Integer.class)))
                .andExpect(jsonPath("$.roleID", is(validRoleID)))
                .andExpect(jsonPath("$.roleName", isA(String.class)));

        this.mockMvc.perform(get("/roles/" + invalidRoleID))
                .andExpect(status().is(400))
                .andExpect(result -> assertInstanceOf(ConstraintViolationException.class, result.getResolvedException()));

        this.mockMvc.perform(get("/roles/" + nonExistantRoleID))
                .andExpect(status().is(404))
                .andExpect(result -> assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @WithMockUser()
    void deleteRoleByID() throws Exception {
        final int validRoleID = 1;
        final int invalidRoleID = -1;
        final int nonExistantRoleID = Integer.MAX_VALUE;

        Mockito.doNothing().when(roleService).deleteRole(validRoleID);
        Mockito.doThrow(ConstraintViolationException.class).when(roleService).deleteRole(invalidRoleID);
        Mockito.doThrow(ResourceNotFoundException.class).when(roleService).deleteRole(nonExistantRoleID);

        this.mockMvc.perform(delete("/roles/" + validRoleID).with(csrf()))
                .andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(delete("/roles/" + invalidRoleID).with(csrf()))
                .andExpect(status().is(400))
                .andExpect(result -> assertInstanceOf(ConstraintViolationException.class, result.getResolvedException()));

        this.mockMvc.perform(delete("/roles/" + nonExistantRoleID).with(csrf()))
                .andExpect(status().is(404))
                .andExpect(result -> assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()));
    }
}