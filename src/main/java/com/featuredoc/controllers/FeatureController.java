
package com.featuredoc.controllers;

import com.featuredoc.dto.FeatureRequest;
import com.featuredoc.models.Feature;
import com.featuredoc.models.FeatureVersion;
import com.featuredoc.services.FeatureService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Optional;

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

    @PutMapping(value = {"", "/"})
    public ResponseEntity<Object> updateFeature(
            @RequestBody FeatureRequest request) {
        try {
            FeatureVersion updatedFeatureVersion = featureService.updateFeature(request);
            return ResponseEntity.ok(updatedFeatureVersion);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
}
