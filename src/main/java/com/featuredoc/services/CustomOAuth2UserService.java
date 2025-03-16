package com.featuredoc.services;

import com.featuredoc.models.CustomOAuth2User;
import com.featuredoc.models.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        System.out.println("Loading from superClass");

        OAuth2User oauth2User = super.loadUser(userRequest);

        String email = oauth2User.getAttribute("email");

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new OAuth2AuthenticationException(
                        new OAuth2Error("401"),
                        "User Not Found"
                ));

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) oauth2User.getAuthorities();

        // Here you can map additional attributes or roles if needed
        // For example, you can assign authorities (roles) to the user.
        return new CustomOAuth2User(user, (Collection<SimpleGrantedAuthority>) oauth2User.getAuthorities());
    }

}
