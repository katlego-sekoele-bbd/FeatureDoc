package com.featuredoc.repository;

import com.featuredoc.models.FeatureView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeatureViewRepository extends JpaRepository<FeatureView, Integer> {
    @Query("SELECT f FROM FeatureView f WHERE f.featureID = :featureID ORDER BY f.featureVersionID DESC LIMIT 1")
    Optional<FeatureView> findLatestVersionByFeatureId(@Param("featureID")  Integer featureID);

    @Query("SELECT f FROM FeatureView f WHERE f.featureID = :featureID ORDER BY f.featureVersionID ASC")
    List<FeatureView> findAllVersionsByFeatureId(@Param("featureID")  Integer featureID);

    @Query(value = """
        SELECT DISTINCT ON (fv."FeatureID") *
        FROM "FeatureView" fv
        ORDER BY fv."FeatureID", fv."UpdatedDate" DESC
    """, nativeQuery = true)
   List<FeatureView> findAllLatestFeatureViews();

}
