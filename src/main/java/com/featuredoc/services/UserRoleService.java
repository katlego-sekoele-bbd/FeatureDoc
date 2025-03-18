package com.featuredoc.services;

import com.featuredoc.models.UserRole;
import com.featuredoc.models.UserRoleId;
import com.featuredoc.repository.UserRoleRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;
    public List<UserRole> getRolesByUserId(Integer userID) {
        return userRoleRepository.findUserRoles(userID);
    }
    public UserRole addUserRole(UserRoleId userRoleId) {
        UserRole userRole = new UserRole();
        userRole.setId(userRoleId);
        return userRoleRepository.save(userRole);
    }
    
}