package com.featuredoc.services;

import com.featuredoc.models.Role;
import com.featuredoc.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
class RoleServiceTest {

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleService roleService;

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
    void getAllRoles() {
        when(roleRepository.findAll())
                .thenReturn(mockRoles);

        var actualRoles = roleService.getAllRoles();

        assertEquals(mockRoles, actualRoles);
    }

    @Test
    void getRoleById() {

        final long validRoleID = 1;
        final long invalidRoleID = -1;
        final long nonExistantRoleID = Integer.MAX_VALUE;

        when(roleRepository.findById(validRoleID))
                .thenReturn(Optional.ofNullable(mockRoles.get((int) (validRoleID - 1))));

        when(roleRepository.findById(invalidRoleID))
                .thenReturn(Optional.empty());

        when(roleRepository.findById(nonExistantRoleID))
                .thenReturn(Optional.empty());

        var actualValidRole = roleService.getRoleById(validRoleID);
        var actualInvalidRole = roleService.getRoleById(invalidRoleID);
        var actualNonExistentRole = roleService.getRoleById(nonExistantRoleID);

        assertEquals(actualValidRole.orElse(null), mockRoles.get((int) (validRoleID - 1)));
        assertEquals(actualInvalidRole, Optional.empty());
        assertEquals(actualNonExistentRole, Optional.empty());
    }

    @Test
    void createRole() {
        when(roleRepository.save(mockCreateRole)).thenReturn(mockCreateRole);

        var actualCreatedRole = roleService.createRole(mockCreateRole);

        assertEquals(actualCreatedRole, mockCreateRole);
    }

    @Test
    void deleteRole() {
        final long validRoleID = 1;
        final long invalidRoleID = -1;
        final long nonExistantRoleID = Integer.MAX_VALUE;

        Mockito.doNothing().when(roleRepository).deleteById(validRoleID);
        Mockito.doNothing().when(roleRepository).deleteById(invalidRoleID);
        Mockito.doNothing().when(roleRepository).deleteById(nonExistantRoleID);

        // nothing really to test here
    }
}