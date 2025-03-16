package com.featuredoc.services;

import com.featuredoc.models.Role;
import com.featuredoc.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(long roleID) {
        return roleRepository.findById(roleID);
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public void deleteRole(long roleID) {
        roleRepository.deleteById(roleID);
    }

}
