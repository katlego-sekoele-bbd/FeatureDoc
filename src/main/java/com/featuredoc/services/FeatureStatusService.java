package com.featuredoc.services;

import com.featuredoc.exceptions.ResourceNotFoundException;
import com.featuredoc.models.FeatureStatus;
import com.featuredoc.models.Priority;
import com.featuredoc.repository.FeatureStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeatureStatusService {

    @Autowired
    FeatureStatusRepository featureStatusRepository;

    public List<FeatureStatus> getAllFeatureStatuses() {
        return featureStatusRepository.findAll();
    }

    public FeatureStatus createFeatureStatus(FeatureStatus featureStatus) {
        return featureStatusRepository.save(featureStatus);
    }

    public Optional<FeatureStatus> getFeatureStatusById(long featureStatusID) {
        return featureStatusRepository.findById(featureStatusID);
    }

    public void deleteFeatureStatusById(long featureStatusID) {
        featureStatusRepository.deleteById(featureStatusID);
    }

}
