package com.featuredoc.services;

import com.featuredoc.models.CustomOAuth2User;
import com.featuredoc.models.User;
import com.featuredoc.models.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Autowired
    private Environment env;

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

        String accessToken = userRequest.getAccessToken().getTokenValue();

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) oauth2User.getAuthorities();

        // Optionally, map more custom attributes or assign roles here
        List<Integer> deletePermittedRoleIDs = Stream.of(env.getProperty("CAN_DELETE").split(",")).map(Integer::valueOf).toList();
        List<UserRole> userRoles = userRoleService.getRolesByUserId(user.getUserID());
        boolean canDelete = userRoles.stream().anyMatch(userRole -> deletePermittedRoleIDs.contains(userRole.getId().getRoleID()));

        if (canDelete) {
             authorities.add(new SimpleGrantedAuthority("CAN_DELETE"));
         } else {
             // this type of user won't have any delete grants
         }
        // Here you can map additional attributes or roles if needed
        // For example, you can assign authorities (roles) to the user.
        return new CustomOAuth2User(user, authorities);
    }

//    public void storeAccessToken(Long userId, String accessToken) {
//        // Example: Store token securely in your database
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        user.setAccessToken(accessToken);
//        userRepository.save(user);
//    }

}
