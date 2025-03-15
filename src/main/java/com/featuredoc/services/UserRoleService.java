package com.featuredoc.services;

import com.featuredoc.models.UserRole;
import com.featuredoc.repository.UserRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {

    @Autowired
    private UserRolesRepository userRolesRepository;

    public List<UserRole> getAllRoles() {
        return userRolesRepository.findAll();
    }
}

