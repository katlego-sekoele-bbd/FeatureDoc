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

        String accessToken = userRequest.getAccessToken().getTokenValue();


        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) oauth2User.getAuthorities();

        // Optionally, map more custom attributes or assign roles here
        // For example, you could add a custom role like this:
        // if (user.getEmail().endsWith("@admin.com")) {
        //     authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        // }
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
