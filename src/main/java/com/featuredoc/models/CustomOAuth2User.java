package com.featuredoc.models;

import com.featuredoc.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomOAuth2User(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        // Map additional attributes here (email, name, etc.)
        return Map.of(
                "email", user.getEmail(),
                "name", user.getName()
        );
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public User getUser() {
        return user;
    }


}
