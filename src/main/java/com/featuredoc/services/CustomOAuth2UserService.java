package com.featuredoc.services;

import com.featuredoc.models.CustomOAuth2User;
import com.featuredoc.models.User;
import com.featuredoc.models.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        System.out.println("Loading from superClass");

        OAuth2User oauth2User = super.loadUser(userRequest);

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("given_name");
        String surname = oauth2User.getAttribute("family_name");

        User user;

        try {
            user = userService.getUserByEmail(email).get();
        }
        catch (NoSuchElementException e) {
            user = userService.createUser(new User(name+ " "+ surname, email));
        }

        List<Integer> deletePermittedRoleIDs = List.of(3,4,6,8,9);

        List<UserRole> userRoles = userRoleService.getRolesByUserId(user.getUserID());
        boolean canDelete = userRoles.stream().anyMatch(userRole -> deletePermittedRoleIDs.contains(userRole.getId().getRoleID()));

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) oauth2User.getAuthorities();
        List<GrantedAuthority> mutableAuthorities = new ArrayList<>(authorities);

         if (canDelete) {
             mutableAuthorities.add(new SimpleGrantedAuthority("ROLE_CAN_DELETE"));
         }
         else {
             mutableAuthorities.add(new SimpleGrantedAuthority("ROLE_CANT_DELETE"));
         }

        return new CustomOAuth2User(user, mutableAuthorities);
    }

}
