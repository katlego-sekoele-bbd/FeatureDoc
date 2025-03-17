package com.featuredoc.services;

import com.featuredoc.dto.FeatureRequest;
import com.featuredoc.models.Feature;
import com.featuredoc.models.FeatureVersion;
import com.featuredoc.services.FeatureVersionService;
import com.featuredoc.repository.FeatureRepository;
import com.featuredoc.repository.FeatureVersionRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeatureService {

    @Autowired
    private FeatureVersionRepository featureVersionRepository;

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private FeatureVersionService featureVersionService;

    @Transactional
    public Feature addFeature(FeatureRequest request) {
        Feature feature = new Feature(request.getCreatedBy());
        feature = featureRepository.save(feature);
        FeatureVersion featureVersion = new FeatureVersion(
                request.getUpdatedBy(),
                feature.getFeatureID(),
                request.getFeatureStatusID(),
                request.getPriorityID(),
                request.getAssignedTo(),
                request.getName(),
                request.getShortDescription(),
                request.getURL());

        featureVersionRepository.save(featureVersion);
        return feature;
    }

    @Transactional
    public FeatureVersion updateFeature(FeatureRequest request) {
        Optional<FeatureVersion> latestVersion = featureVersionService
                .getLatestFeatureVersionByFeatureId(request.getFeatureID());

        FeatureVersion latestFeatureVersion = latestVersion
                .orElseThrow(() -> new RuntimeException(
                        "No previous version found for Feature ID: " + request.getFeatureID()));

        FeatureVersion newVersion = new FeatureVersion(
                request.getUpdatedBy(),
                request.getFeatureID(),
                request.getFeatureStatusID() != null ? request.getFeatureStatusID()
                        : latestFeatureVersion.getFeatureStatusID(),
                request.getPriorityID() != null ? request.getPriorityID() : latestFeatureVersion.getPriorityID(),
                request.getAssignedTo() != null ? request.getAssignedTo() : latestFeatureVersion.getAssignedTo(),
                request.getName() != null ? request.getName() : latestFeatureVersion.getName(),
                request.getShortDescription() != null ? request.getShortDescription()
                        : latestFeatureVersion.getShortDescription(),
                request.getURL() != null ? request.getURL() : latestFeatureVersion.getURL());
        return featureVersionRepository.save(newVersion);
    }
}
