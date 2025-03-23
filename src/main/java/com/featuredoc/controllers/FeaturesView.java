package com.featuredoc.controllers;

import com.featuredoc.services.FeatureViewService;
import com.featuredoc.models.FeatureView;
import org.springframework.http.ResponseEntity;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/feature-versions")
@Validated
public class FeaturesView {

    @Autowired
    private FeatureViewService featureViewService;

    @GetMapping("/{featureID}")
    public FeatureView getLatestFeatureVersion(
            @PathVariable("featureID") @Min(value = 1, message = "featureID must be a positive integer") Integer featureID) {
        return featureViewService.getLatestFeatureVersionByFeatureId(featureID)
                .orElse(new FeatureView());
    }

    @GetMapping
    public List<FeatureView> getAllLatestFeatureVersions() {
        return featureViewService.getAllLatestFeatureViews();
    }

    @GetMapping("/{featureID}/history")
    public ResponseEntity<List<FeatureView>> getAllFeatureVersions(
            @PathVariable("featureID") @Min(value = 1, message = "featureID must be a positive integer") Integer featureID) {
        List<FeatureView> featureVersions = featureViewService.getAllVersionsByFeatureId(featureID);
        if (featureVersions.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(featureVersions);
    }

}
