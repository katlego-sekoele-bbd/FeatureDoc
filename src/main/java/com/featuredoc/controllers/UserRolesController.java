package com.featuredoc.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.featuredoc.models.UserRole;
import com.featuredoc.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class UserRolesController {

    @Autowired
    private UserRoleService userRoleService;

    @GetMapping("/roles")
    public List<UserRole> getAllRoles() {
        return userRoleService.getAllRoles();
    }
}
