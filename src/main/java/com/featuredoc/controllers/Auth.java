package com.featuredoc.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class Auth {

    //Logic below needs to be refined to check the email we get from google against our database before considering the authentication successfull
    //Currently the Automatic configuration from springs considers it successful as long as the user signs in with a valid gmail account.
    @GetMapping("/login")
    public String grantCode(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient, @AuthenticationPrincipal OAuth2User oauth2User) {
        System.out.println(oauth2User.getName());
        System.out.println(authorizedClient.getClientRegistration());
        System.out.println(oauth2User.getAttributes());

        return "index";
    }

}
