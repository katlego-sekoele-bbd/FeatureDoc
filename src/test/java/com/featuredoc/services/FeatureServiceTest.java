package com.featuredoc.services;

import com.featuredoc.dto.FeatureRequest;
import com.featuredoc.models.Feature;
import com.featuredoc.models.FeatureVersion;
import com.featuredoc.models.FeatureView;
import com.featuredoc.repository.FeatureRepository;
import com.featuredoc.repository.FeatureStatusRepository;
import com.featuredoc.repository.FeatureVersionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
class FeatureServiceTest {

    @Mock
    FeatureVersionRepository featureVersionRepository;

    @Mock
    FeatureRepository featureRepository;

    @InjectMocks
    FeatureVersionService featureVersionService;

    @InjectMocks
    FeatureService featureService;

    FeatureRequest mockFeatureRequest = new FeatureRequest(1, 1,1,1,1,1,"1","1", "1");
    FeatureRequest mockFeatureRequestUpdate = new FeatureRequest(2, 1,2,2,2,2,"2","2", "2");
    Feature mockFeature = new Feature(1, 1, Timestamp.from(Instant.now()));
    Feature mockMinimalFeature = new Feature(1);
    FeatureVersion mockFeatureVersion = new FeatureVersion(
            mockFeatureRequest.getUpdatedBy(),
            mockFeature.getFeatureID(),
            mockFeatureRequest.getFeatureStatusID(),
            mockFeatureRequest.getPriorityID(),
            mockFeatureRequest.getAssignedTo(),
            mockFeatureRequest.getName(),
            mockFeatureRequest.getShortDescription(),
            mockFeatureRequest.getURL()
    );
    FeatureVersion mockUpdateFeatureVersion = new FeatureVersion(
            mockFeatureRequestUpdate.getUpdatedBy(),
            mockFeatureRequestUpdate.getFeatureID(),
            mockFeatureRequestUpdate.getFeatureStatusID(),
            mockFeatureRequestUpdate.getPriorityID(),
            mockFeatureRequestUpdate.getAssignedTo(),
            mockFeatureRequestUpdate.getName(),
            mockFeatureRequestUpdate.getShortDescription(),
            mockFeatureRequestUpdate.getURL()
    );

    @Test
    void addFeature() {
        when(featureRepository.save(mockMinimalFeature)).thenReturn(mockFeature);
        when(featureVersionRepository.save(mockFeatureVersion)).thenReturn(mockFeatureVersion);

        var actual = featureService.addFeature(mockFeatureRequest);

        assertEquals(mockFeature, actual);
    }

    @Test
    void updateFeature() {
        when(featureVersionService.getLatestFeatureVersionByFeatureId(mockFeatureRequestUpdate.getFeatureID()))
                .thenReturn(Optional.of(mockFeatureVersion));

        when(featureVersionRepository.save(mockUpdateFeatureVersion)).thenReturn(mockUpdateFeatureVersion);

        var actual = featureService.updateFeature(mockFeatureRequestUpdate);

        assertEquals(mockUpdateFeatureVersion, actual);
    }

    @Test
    void failUpdateFeature() {

    }


    private final List<FeatureView> mockFeatures = List.of(
            new FeatureView(1,
                    "feature 1",
                    "Person 1",
                    LocalDateTime.now(),
                    1,
                    "Person 1",
                    "In Progress",
                    "High",
                    "Person 1",
                    "Feature",
                    "Description",
                    "url",
                    LocalDateTime.now(),
                    null
            ),
            new FeatureView(2,
                    "feature 2",
                    "Person 2",
                    LocalDateTime.now(),
                    2,
                    "Person 2",
                    "In Progress",
                    "High",
                    "Person 2",
                    "Feature",
                    "Description",
                    "url",
                    LocalDateTime.now(),
                    null
            ),
            new FeatureView(3,
                    "feature 3",
                    "Person 3",
                    LocalDateTime.now(),
                    3,
                    "Person 3",
                    "In Progress",
                    "High",
                    "Person 3",
                    "Feature",
                    "Description",
                    "url",
                    LocalDateTime.now(),
                    null
            )
    );
}