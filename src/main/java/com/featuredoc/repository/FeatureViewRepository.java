package com.featuredoc.repository;

import com.featuredoc.models.FeatureView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeatureViewRepository extends JpaRepository<FeatureView, Integer> {
    // @Query("SELECT f FROM featureview f WHERE f.featureID = :featureID ORDER BY f.featureVersionID DESC")
    // Optional<FeatureView> findById(Integer featureID);

}
