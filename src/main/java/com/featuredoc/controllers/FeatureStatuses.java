package com.featuredoc.controllers;

import com.featuredoc.models.FeatureStatus;
import com.featuredoc.repository.FeatureRepository;
import com.featuredoc.services.FeatureStatusService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feature-statuses")
@Validated
public class FeatureStatuses {

    @Autowired
    FeatureStatusService featureStatusService;

    @Autowired
    FeatureRepository featureRepository;

    @GetMapping(value = {"/", ""})
    public List<FeatureStatus> getAllFeatureStatuses() {
        return featureStatusService.getAllFeatureStatuses();
    }

    @PostMapping(value = {"/", ""})
    public FeatureStatus createFeatureStatus(@RequestBody FeatureStatus featureStatus) {
        return featureStatusService.createFeatureStatus(featureStatus);
    }

    @GetMapping("/{featureStatusID}")
    public FeatureStatus getPriorityById(
            @PathVariable("featureStatusID") @Min(value = 1, message = "featureStatusID must be a positive integer") long featureStatusID) {
        return featureStatusService.getFeatureStatusById(featureStatusID)
                .orElse(new FeatureStatus());
    }

    @DeleteMapping("/{featureStatusID}")
    public ResponseEntity<Void> deletePriorityById(
            @PathVariable("featureStatusID") @Min(value = 1, message = "featureStatusID must be a positive integer") Integer featureStatusID) {
        if (!featureRepository.existsById((featureStatusID))) {
            throw new DataIntegrityViolationException(
                    "featureStatusID " + featureStatusID + " does not exist in the database");
        } else {
            featureStatusService.deleteFeatureStatusById(featureStatusID);
            return ResponseEntity.noContent().build();

        }

    }

}
