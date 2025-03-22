package com.featuredoc.services;

import com.featuredoc.models.User;
import com.featuredoc.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

        assertThrows(NoSuchElementException.class, () -> userService.getUserById(-1L));
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
    }

    @Test
    public void deleteUserNullUser() {
        doThrow(NoSuchElementException.class)
                .when(userRepository)
                        .deleteById(-1L);

        assertThrows(NoSuchElementException.class, () -> userService.deleteUser(-1L));
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

        assertThrows(NoSuchElementException.class, () -> userService.getUserByEmail(""));
    }

//    @Test
//    public void getCurrentUserBestCase() {
//        final boolean loggedIn = true;
//        final String principle = "test@test.com";
//        final String name = "test";
//        User user = new User(1L, name, principle);
//        SecurityContextHolder.setContext(new SecurityContext() {
//            @Override
//            public Authentication getAuthentication() {
//                return new Authentication() {
//                    @Override
//                    public Collection<? extends GrantedAuthority> getAuthorities() {
//                        return List.of();
//                    }
//
//                    @Override
//                    public Object getCredentials() {
//                        return null;
//                    }
//
//                    @Override
//                    public Object getDetails() {
//                        return null;
//                    }
//
//                    @Override
//                    public Object getPrincipal() {
//                        return principle;
//                    }
//
//                    @Override
//                    public boolean isAuthenticated() {
//                        return loggedIn;
//                    }
//
//                    @Override
//                    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
//
//                    }
//
//                    @Override
//                    public String getName() {
//                        return name;
//                    }
//                };
//            }
//
//            @Override
//            public void setAuthentication(Authentication authentication) {
//
//            }
//        });
//
//        when(userService.getUserByEmail(principle))
//                .thenReturn(Optional.of(user));
//
//        User actual = userService.getCurrentUser();
//
//        assertEquals(user, actual);
//    }
//
//    @Test
//    public void getCurrentUserNotLoggedIn() {
//        final boolean loggedIn = false;
//        final String principle = null;
//        final String name = null;
//        User user = new User(1L, name, principle);
//        SecurityContextHolder.setContext(new SecurityContext() {
//            @Override
//            public Authentication getAuthentication() {
//                return new Authentication() {
//                    @Override
//                    public Collection<? extends GrantedAuthority> getAuthorities() {
//                        return List.of();
//                    }
//
//                    @Override
//                    public Object getCredentials() {
//                        return null;
//                    }
//
//                    @Override
//                    public Object getDetails() {
//                        return null;
//                    }
//
//                    @Override
//                    public Object getPrincipal() {
//                        return principle;
//                    }
//
//                    @Override
//                    public boolean isAuthenticated() {
//                        return loggedIn;
//                    }
//
//                    @Override
//                    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
//
//                    }
//
//                    @Override
//                    public String getName() {
//                        return name;
//                    }
//                };
//            }
//
//            @Override
//            public void setAuthentication(Authentication authentication) {
//
//            }
//        });
//
//        assertThrows(AuthenticationException.class, () -> userService.getCurrentUser());
//    }
}