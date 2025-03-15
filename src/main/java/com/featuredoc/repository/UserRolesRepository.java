package com.featuredoc.repository;

import com.featuredoc.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRole,Integer> {
    // Custom query methods can be defined here, if needed
}
