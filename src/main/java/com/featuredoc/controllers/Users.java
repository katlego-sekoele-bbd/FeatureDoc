package com.featuredoc.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class Users {

    @RequestMapping("/users")
    public String home() {
        return "Let's pretend this is a list of users";
    }

}
