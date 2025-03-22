package com.featuredoc.services;

import com.featuredoc.models.Role;
import com.featuredoc.repository.PriorityRepository;
import com.featuredoc.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void getAllRolesBestCase() {
        List<Role> roles = List.of(
                new Role(1, "1"),
                new Role(2, "2"),
                new Role(3, "3")
        );

        when(roleRepository.findAll())
                .thenReturn(roles);

        List<Role> actual = roleService.getAllRoles();

        assertEquals(roles, actual);
    }

    @Test
    public void getAllRolesNoneInDB() {
        List<Role> roles = List.of();

        when(roleRepository.findAll())
                .thenReturn(roles);

        List<Role> actual = roleService.getAllRoles();

        assertEquals(roles, actual);
    }

    @Test
    public void createRoleBestCase() {
        Role role = new Role(1, "1");

        when(roleRepository.save(role))
                .thenReturn(role);

        Role actual = roleService.createRole(role);

        assertEquals(role, actual);
    }

    @Test
    public void createRoleNullRole() {
        Role role = null;

        assertThrows(IllegalArgumentException.class, () -> roleService.createRole(role));
    }

    @Test
    public void createRoleRoleExists() {
        List<Role> roles = List.of(
                new Role(1, "1")
        );
        Role role = new Role("1");

        assertThrows(DataIntegrityViolationException.class, () -> roleService.createRole(role));
    }

    @Test
    public void getRoleByIDBestCase() {
        Role role = new Role(1, "1");

        when(roleRepository.findById(Long.valueOf(role.getRoleID())))
                .thenReturn(Optional.of(role));

        Optional<Role> actual = roleService.getRoleById(role.getRoleID());

        assertEquals(Optional.of(role), actual);
    }

    @Test
    public void getRoleByIDRoleNotExist() {
        when(roleRepository.findById(-1L))
                .thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> roleService.getRoleById(-1L));
    }

    @Test
    public void deleteRoleBestCase() {
        Role role = new Role(1, "1");

        doNothing().when(roleRepository).deleteById(Long.valueOf(role.getRoleID()));

        roleService.deleteRole(Long.valueOf(role.getRoleID()));
        verify(roleRepository, times(1)).deleteById(Long.valueOf(role.getRoleID()));
    }

    @Test
    public void deleteRoleNotExist() {
        doThrow(NoSuchElementException.class).when(roleRepository).deleteById(-1L);

        assertThrows(NoSuchElementException.class, () -> roleService.deleteRole(-1L));
        verify(roleRepository, never()).deleteById(-1L);
    }

}