package com.featuredoc.controllers;

import com.featuredoc.exceptions.ResourceNotFoundException;
import com.featuredoc.services.FeatureViewService;
import com.featuredoc.models.FeatureView;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feature")
@CrossOrigin
@Validated
public class FeaturesView{

    @Autowired
    private FeatureViewService featureViewService;
   
    @GetMapping("/{featureID}")
    public FeatureView getFeatureById(
            @PathVariable("featureID")
            @Min(value = 1, message = "featureID must be a positive integer")
            Integer featureID
    ) {
        return featureViewService.getFeatureById(featureID)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Could not find role featureID=%s", featureID)));
    }
   
}
