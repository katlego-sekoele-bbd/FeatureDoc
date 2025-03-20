package com.featuredoc.controllers;

import com.featuredoc.exceptions.ResourceNotFoundException;
import com.featuredoc.models.FeatureStatus;
import com.featuredoc.models.Priority;
import com.featuredoc.services.FeatureStatusService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feature-statuses")
@CrossOrigin
@Validated
public class FeatureStatuses {

    @Autowired
    FeatureStatusService featureStatusService;

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
            @PathVariable("featureStatusID")
            @Min(value = 1, message = "featureStatusID must be a positive integer")
            long featureStatusID)
    {
        return featureStatusService.getFeatureStatusById(featureStatusID)
                .orElseThrow(() -> new ResourceNotFoundException("FeatureStatus", "featureStatusID", featureStatusID));
    }

    @DeleteMapping("/{featureStatusID}")
    public ResponseEntity<Object> deletePriorityById(
            @PathVariable("featureStatusID")
            @Min(value = 1, message = "featureStatusID must be a positive integer")
            long featureStatusID
    ) {
        featureStatusService.deleteFeatureStatusById(featureStatusID);
        return ResponseEntity.noContent().build();
    }

}
