package com.featuredoc.controllers;

import com.featuredoc.exceptions.ResourceNotFoundException;
import com.featuredoc.services.FeatureViewService;
import com.featuredoc.models.FeatureView;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/feature-versions")
@CrossOrigin
@Validated
public class FeaturesView {

    @Autowired
    private FeatureViewService featureViewService;

    @GetMapping("/{featureID}")
    public FeatureView getLatestFeatureVersion(
            @PathVariable("featureID") @Min(value = 1, message = "featureID must be a positive integer") Integer featureID) {
        return featureViewService.getLatestFeatureVersionByFeatureId(featureID)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Could not feature with id=%s", featureID)));
    }

    @GetMapping("/{featureID}/history")
    public ResponseEntity<List<FeatureView>> getAllFeatureVersions(
            @PathVariable("featureID") @Min(value = 1, message = "featureID must be a positive integer") Integer featureID) {
        List<FeatureView> featureVersions = featureViewService.getAllVersionsByFeatureId(featureID);

        if (featureVersions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } else {
            return ResponseEntity.ok(featureVersions);

        }

    }

}
