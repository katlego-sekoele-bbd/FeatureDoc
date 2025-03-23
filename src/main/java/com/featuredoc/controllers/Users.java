package com.featuredoc.controllers;

import com.featuredoc.services.EmailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.featuredoc.models.User;
import com.featuredoc.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
class Users {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/sendEmail")
    public void sendEmail () {
        emailService.sendSimpleMessage("keith.hughes@bbd.co.za", "Test", "This is a test message");
    }

    @GetMapping("/users/{userID}")
    public User getUserById(@PathVariable("userID") Long userID) {
        return userService.getUserById(userID).orElse(new User());
    }


    @PostMapping("/register")
    public String registerUser(@RequestBody User request) {
        // Process the registration request
        String name = request.getName();
        String email = request.getEmail();
        // Perform business logic (e.g., save to database, send confirmation email, etc.)
        System.out.println("Registering user: " + name + " with email: " + email);
        // Return a response
        return "User registered successfully!";
    }





}
