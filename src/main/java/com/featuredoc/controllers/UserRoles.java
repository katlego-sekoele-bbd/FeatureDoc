package com.featuredoc.controllers;

import com.featuredoc.models.UserRole;
import com.featuredoc.models.UserRoleId;
import com.featuredoc.services.UserRoleService;

import java.util.List;

import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-roles")
@Validated
public class UserRoles {

    @Autowired
    private UserRoleService userRoleService;

    @GetMapping("/user/{userID}")
    public ResponseEntity<List<UserRole>> getRolesByUserId(
            @PathVariable 
            @Min(value = 1, message = "userID must be a positive integer") Long userID) {
        List<UserRole> userRoles = userRoleService.getRolesByUserId(userID);
        return ResponseEntity.ok(userRoles);
    }

    @PostMapping
    public ResponseEntity<UserRole> addUserRole(@RequestBody UserRoleId userRoleId) {
        UserRole userRole = userRoleService.addUserRole(userRoleId);
        return ResponseEntity.ok(userRole);
    }
}