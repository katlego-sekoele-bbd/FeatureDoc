package com.featuredoc.services;

import com.featuredoc.models.UserRole;
import com.featuredoc.models.UserRoleId;
import com.featuredoc.repository.RoleRepository;
import com.featuredoc.repository.UserRepository;
import com.featuredoc.repository.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
class UserRoleServiceTest {

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRoleService userRoleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void getRolesByUserIdBestCase() {
        List<UserRole> userRoles = List.of(
                new UserRole(new UserRoleId(1,1)),
                new UserRole(new UserRoleId(2,1)),
                new UserRole(new UserRoleId(3,1))
        );

        when(userRoleRepository.findUserRoles(Long.valueOf(userRoles.get(0).getId().getUserID())))
                .thenReturn(userRoles);

        List<UserRole> actual = userRoleService.getRolesByUserId(Long.valueOf(userRoles.get(0).getId().getUserID()));

        assertEquals(userRoles, actual);
    }

    @Test
    public void getRolesByUserIdNoRoles() {
        List<UserRole> userRoles = List.of();

        when(userRoleRepository.findUserRoles(1L))
                .thenReturn(userRoles);

        List<UserRole> actual = userRoleService.getRolesByUserId(1L);

        assertEquals(userRoles, actual);
    }

    @Test
    public void getRolesByUserIdNoUser() {
        List<UserRole> userRoles = List.of();

        when(userRepository.findById(-1L))
                .thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> userRoleService.getRolesByUserId(-1L));
    }

    @Test
    public void addUserRoleBestCase() {
        UserRoleId userRoleId = new UserRoleId(1,1);
        UserRole userRole = new UserRole(userRoleId);

        when(userRoleRepository.save(userRole))
                .thenReturn(userRole);

        UserRole actual = userRoleService.addUserRole(userRoleId);

        assertEquals(userRole, actual);
    }

    @Test
    public void addUserRoleNoUser() {
        UserRoleId userRoleId = new UserRoleId(1,-1);
        UserRole userRole = new UserRole(userRoleId);

        when(userRepository.findById(Long.valueOf(userRoleId.getUserID())))
                .thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> userRoleService.addUserRole(userRoleId));
    }

    @Test
    public void addUserRoleNoRole() {
        UserRoleId userRoleId = new UserRoleId(-1,1);
        UserRole userRole = new UserRole(userRoleId);

        when(roleRepository.findById(Long.valueOf(userRoleId.getRoleID())))
                .thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> userRoleService.addUserRole(userRoleId));
    }
}