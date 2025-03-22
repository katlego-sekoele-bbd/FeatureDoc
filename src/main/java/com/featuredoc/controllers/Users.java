package com.featuredoc.controllers;

import com.featuredoc.services.EmailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.featuredoc.models.User;
import com.featuredoc.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    // Get a user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
