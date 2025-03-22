package com.featuredoc.services;

import com.featuredoc.models.UserRole;
import com.featuredoc.models.UserRoleId;
import com.featuredoc.repository.RoleRepository;
import com.featuredoc.repository.UserRepository;
import com.featuredoc.repository.UserRoleRepository;

import java.util.List;

import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    public List<UserRole> getRolesByUserId(Long userID) {
        return userRoleRepository.findUserRoles(userID);
    }
    public UserRole addUserRole(UserRoleId userRoleId) {
        UserRole userRole = new UserRole();
        userRole.setId(userRoleId);
        if(userRoleRepository.existsById(userRoleId)){
            throw new DataIntegrityViolationException("User role has already been added for user");
        }
        else if(!userRepository.existsById(userRoleId.getUserID())){
            throw new DataIntegrityViolationException("Given userID " + userRoleId.getUserID() + " does not exist in the database");
        }
        else if(!roleRepository.existsById((userRoleId.getRoleID()))){
            throw new DataIntegrityViolationException("Given roleID " + userRoleId.getRoleID() + " does not exist in the database");
        }
        return userRoleRepository.save(userRole);
    }
    
}