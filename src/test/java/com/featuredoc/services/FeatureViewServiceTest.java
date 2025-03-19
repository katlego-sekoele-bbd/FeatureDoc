package com.featuredoc.services;

import com.featuredoc.models.FeatureView;
import com.featuredoc.repository.FeatureStatusRepository;
import com.featuredoc.repository.FeatureViewRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
class FeatureViewServiceTest {

    @Mock
    FeatureViewRepository featureViewRepository;

    @InjectMocks
    FeatureViewService featureViewService;

    @Test
    void getLatestFeatureVersionByFeatureId() {
        int validId = 1;
        int invalidId = -1;
        int nullId = Integer.MAX_VALUE;

        when(featureViewRepository.findLatestVersionByFeatureId(validId))
                .thenReturn(Optional.of(mockFeatures.get(validId - 1)));

        when(featureViewRepository.findLatestVersionByFeatureId(invalidId))
                .thenReturn(Optional.empty());

        when(featureViewRepository.findLatestVersionByFeatureId(nullId))
                .thenReturn(Optional.empty());

        var actualValid = featureViewService.getLatestFeatureVersionByFeatureId(validId);
        var actualInvalid = featureViewService.getLatestFeatureVersionByFeatureId(invalidId);
        var actualNull = featureViewService.getLatestFeatureVersionByFeatureId(nullId);

        assertEquals(mockFeatures.get(validId - 1), actualValid.orElse(null));
        assertEquals(null, actualInvalid.orElse(null));
        assertEquals(null, actualNull.orElse(null));

    }

    @Test
    void getAllVersionsByFeatureId() {
        int validId = 1;
        int invalidId = -1;
        int nullId = Integer.MAX_VALUE;

        when(featureViewRepository.findAllVersionsByFeatureId(validId))
                .thenReturn(mockFeatures);

        when(featureViewRepository.findAllVersionsByFeatureId(invalidId))
                .thenReturn(List.of());

        when(featureViewRepository.findAllVersionsByFeatureId(nullId))
                .thenReturn(List.of());

        var actualValid = featureViewService.getAllVersionsByFeatureId(validId);
        var actualInvalid = featureViewService.getAllVersionsByFeatureId(invalidId);
        var actualNull = featureViewService.getAllVersionsByFeatureId(nullId);

        assertEquals(mockFeatures, actualValid);
        assertEquals(0, actualInvalid.size());
        assertEquals(0, actualNull.size());
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