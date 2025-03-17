
package com.featuredoc.services;
import com.featuredoc.models.FeatureVersion;
import com.featuredoc.repository.FeatureVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeatureVersionService {

    @Autowired
    FeatureVersionRepository featureVersionRepository;

    public Optional<FeatureVersion> getLatestFeatureVersionByFeatureId(Integer featureID) {
        return featureVersionRepository.getLatestVersionByFeatureId((featureID));
    }

}


