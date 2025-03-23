package com.featuredoc.services;

import com.featuredoc.models.User;
import com.featuredoc.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void getAllUsersBestCase() {
        List<User> users = List.of(
                new User(1L, "name", "email"),
                new User(2L, "name 2", "email"),
                new User(3L, "name 3", "email")
        );

        when(userRepository.findAll())
                .thenReturn(users);

        List<User> actual = userService.getAllUsers();

        assertEquals(users, actual);
    }

    @Test
    public void getAllUsersNoUsersInDB() {
        List<User> users = List.of();

        when(userRepository.findAll())
                .thenReturn(users);

        List<User> actual = userService.getAllUsers();

        assertEquals(users, actual);
    }

    @Test
    public void getUserByIdBestCase() {
        User user = new User(1L, "name", "email");

        when(userRepository.findById(user.getUserID()))
                .thenReturn(Optional.of(user));

        Optional<User> actual = userService.getUserById(user.getUserID());

        assertEquals(Optional.of(user), actual);
    }

    @Test
    public void getUserByIdNoUser() {
        when(userRepository.findById(-1L))
                .thenReturn(Optional.empty());

        Optional<User> actual = userService.getUserById(-1L);

        assertEquals(Optional.empty(), actual);
    }

    @Test
    public void createUserBestCase() {
        User user = new User(1L, "name", "email");

        when(userRepository.save(user))
                .thenReturn(user);

        User actual = userService.createUser(user);

        assertEquals(user, actual);
    }

    @Test
    public void createUserNullUser() {
        User user = null;

        when(userRepository.save(user))
                .thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    @Test
    public void deleteUserBestCase() {
        User user = new User(1L, "name", "email");

        doNothing().when(userRepository).deleteById(user.getUserID());

        userService.deleteUser(user.getUserID());
        verify(userRepository, times(1)).deleteById(user.getUserID());
    }

    @Test
    public void deleteUserNullUser() {
        doNothing().when(userRepository).deleteById(-1L);

        userService.deleteUser(-1L);

        verify(userRepository, times(1)).deleteById(-1L);
    }

    @Test
    public void getUserByEmailBestCase() {
        User user = new User(1L, "name", "email");

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        Optional<User> actual = userService.getUserByEmail(user.getEmail());

        assertEquals(Optional.of(user), actual);
    }

    @Test
    public void getUserByEmailNoUser() {
        when(userRepository.findByEmail(""))
                .thenReturn(Optional.empty());

        Optional<User> actual = userService.getUserByEmail("");

        assertEquals(Optional.empty(), actual);
        verify(userRepository, times(1)).findByEmail("");
    }

    @Test
    public void getCurrentUserBestCase() {
        final boolean loggedIn = true;
        final String principle = "test@test.com";
        final String name = "test";
        User user = new User(1L, name, principle);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal())
                .thenReturn(new Jwt(
                        "test",
                        Instant.now(),
                        Instant.MAX,
                        Map.of("header", "header"),
                        Map.of("email", "test@test.com")
                ));
        SecurityContextHolder.setContext(securityContext);

        when(userService.getUserByEmail(principle))
                .thenReturn(Optional.of(user));

        User actual = userService.getCurrentUser();

        assertEquals(user, actual);
    }

    @Test
    public void getCurrentUserNotLoggedIn() {
        final boolean loggedIn = false;
        final String principle = null;
        final String name = null;
        User user = new User(1L, name, principle);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal())
                .thenReturn(new Jwt(
                        "test",
                        Instant.now(),
                        Instant.MAX,
                        Map.of("header", "header"),
                        Map.of("email", "")
                ));
        SecurityContextHolder.setContext(securityContext);

        when(userService.getUserByEmail(principle))
                .thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> userService.getCurrentUser());
    }
}