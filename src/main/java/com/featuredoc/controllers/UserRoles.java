package com.featuredoc.controllers;

import com.featuredoc.exceptions.ResourceNotFoundException;
import com.featuredoc.models.UserRole;
import com.featuredoc.models.UserRoleId;
import com.featuredoc.services.UserRoleService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-roles")
@CrossOrigin
public class UserRoles {

    @Autowired
    private UserRoleService userRoleService;
    @GetMapping("/user/{userID}")
    public ResponseEntity<List<UserRole>> getRolesByUserId(@PathVariable("userID") Integer userID) {
        List<UserRole> userRoles = userRoleService.getRolesByUserId(userID);
        return ResponseEntity.ok(userRoles); 
    }
    @PostMapping
    public ResponseEntity<UserRole> addUserRole(@RequestBody UserRoleId userRoleId) {
        UserRole userRole = userRoleService.addUserRole(userRoleId);
        return ResponseEntity.ok(userRole);
    }
}