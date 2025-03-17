package com.featuredoc.repository;

import com.featuredoc.models.FeatureStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureStatusRepository extends JpaRepository<FeatureStatus, Long> {
}
