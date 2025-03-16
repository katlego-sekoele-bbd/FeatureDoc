package com.featuredoc.repository;

import com.featuredoc.models.Priority;
import com.featuredoc.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {
}
