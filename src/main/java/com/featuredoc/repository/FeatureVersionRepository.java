package com.featuredoc.repository;


import com.featuredoc.models.FeatureVersion;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureVersionRepository extends JpaRepository<FeatureVersion, Integer> {
    @Query("SELECT fv FROM FeatureVersion fv WHERE fv.featureID = :featureID ORDER BY fv.featureVersionID DESC LIMIT 1")
    Optional<FeatureVersion> getLatestVersionByFeatureId(@Param("featureID") Long featureID);
}
