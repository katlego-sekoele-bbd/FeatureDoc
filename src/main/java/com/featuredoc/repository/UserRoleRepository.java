package com.featuredoc.repository;

import com.featuredoc.models.UserRole;
import com.featuredoc.models.UserRoleId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    // Custom query methods can be added here if needed
     @Query("SELECT ur FROM UserRole ur WHERE ur.id.userID = :userID")
    List<UserRole> findUserRoles(@Param("userID") Long userID);
}