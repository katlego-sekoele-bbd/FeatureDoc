
package com.featuredoc.services;
import com.featuredoc.models.FeatureView;
import com.featuredoc.repository.FeatureViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeatureViewService {

    @Autowired
    FeatureViewRepository featureViewRepository;

    public Optional<FeatureView> getLatestFeatureVersionByFeatureId(Integer featureID) {
        return featureViewRepository.findLatestVersionByFeatureId(featureID);
    }

    public List<FeatureView> getAllVersionsByFeatureId(Integer featureID) {
        return featureViewRepository.findAllVersionsByFeatureId(featureID);
    }

    public List<FeatureView> getAllLatestFeatureViews() {
        return featureViewRepository.findAllLatestFeatureViews();
    }

}
