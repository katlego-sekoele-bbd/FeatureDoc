package com.featuredoc.controllers;

import com.featuredoc.exceptions.ResourceNotFoundException;
import com.featuredoc.models.Role;
import com.featuredoc.services.RoleService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@CrossOrigin
@Validated
public class Roles {

    @Autowired
    private RoleService roleService;

    @GetMapping(value = {"", "/"})
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping(value = {"", "/"})
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    @GetMapping("/{roleID}")
    public Role getRoleByID(
            @PathVariable("roleID")
            @Min(value = 1, message = "roleID must be a positive integer")
            long roleID
    ) {
        return roleService.getRoleById(roleID)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Could not find role roleID=%s", roleID)));
    }

    @DeleteMapping("/{roleID}")
    @PreAuthorize("hasRole('CAN_DELETE')")
    public ResponseEntity deleteRoleByID(@PathVariable("roleID") long roleID) {
        try {
            System.out.println("reached roles delete");
            roleService.deleteRole(roleID);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
