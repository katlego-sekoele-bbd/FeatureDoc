
package com.featuredoc.controllers;

import com.featuredoc.dto.FeatureRequest;
import com.featuredoc.models.Feature;
import com.featuredoc.models.FeatureVersion;
import com.featuredoc.services.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feature")
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    @PostMapping
    public ResponseEntity<Feature> addFeature(@RequestBody FeatureRequest featureRequest) {
        Feature feature = featureService.addFeature(featureRequest);
        return ResponseEntity.ok(feature);
    }
    @PutMapping
    public ResponseEntity<FeatureVersion> updateFeature(
            @RequestBody FeatureRequest request) {

        FeatureVersion updatedFeatureVersion = featureService.updateFeature( request);
        return ResponseEntity.ok(updatedFeatureVersion);
    }
}
