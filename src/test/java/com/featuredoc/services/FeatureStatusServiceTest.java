package com.featuredoc.services;

import com.featuredoc.models.FeatureStatus;
import com.featuredoc.repository.FeatureStatusRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
class FeatureStatusServiceTest {

    @Mock
    FeatureStatusRepository featureStatusRepository;

    @InjectMocks
    FeatureStatusService featureStatusService;

    private final List<FeatureStatus> mockFeatureStatuses = List.of(
            new FeatureStatus(1, "FeatureStatus 1"),
            new FeatureStatus(2, "FeatureStatus 2"),
            new FeatureStatus(3, "FeatureStatus 3"),
            new FeatureStatus(4, "FeatureStatus 4"),
            new FeatureStatus(5, "FeatureStatus 5")
    );

    private final FeatureStatus mockCreateFeatureStatus = new FeatureStatus(6, "FeatureStatus 6");

    @Test
    void getAllFeatureStatuses() {
        when(featureStatusRepository.findAll())
                .thenReturn(mockFeatureStatuses);

        var actualFeatureStatus = featureStatusService.getAllFeatureStatuses();

        assertEquals(mockFeatureStatuses, actualFeatureStatus);
    }

    @Test
    void createFeatureStatus() {
        when(featureStatusRepository.save(mockCreateFeatureStatus)).thenReturn(mockCreateFeatureStatus);

        var actualCreatedRole = featureStatusService.createFeatureStatus(mockCreateFeatureStatus);

        assertEquals(actualCreatedRole, mockCreateFeatureStatus);
    }

    @Test
    void getFeatureStatusById() {
        final long validFeatureStatusID = 1;
        final long invalidFeatureStatusID = -1;
        final long nonExistentFeatureStatusID = Integer.MAX_VALUE;

        when(featureStatusRepository.findById(validFeatureStatusID))
                .thenReturn(Optional.ofNullable(mockFeatureStatuses.get((int) (validFeatureStatusID - 1))));

        when(featureStatusRepository.findById(invalidFeatureStatusID))
                .thenReturn(Optional.empty());

        when(featureStatusRepository.findById(nonExistentFeatureStatusID))
                .thenReturn(Optional.empty());

        var actualValidFeatureStatus = featureStatusService.getFeatureStatusById(validFeatureStatusID);
        var actualInvalidFeatureStatus = featureStatusService.getFeatureStatusById(invalidFeatureStatusID);
        var actualNonExistentFeatureStatus = featureStatusService.getFeatureStatusById(nonExistentFeatureStatusID);

        assertEquals(actualValidFeatureStatus.orElse(null), mockFeatureStatuses.get((int) (validFeatureStatusID - 1)));
        assertEquals(actualInvalidFeatureStatus, Optional.empty());
        assertEquals(actualNonExistentFeatureStatus, Optional.empty());
    }

    @Test
    void deleteFeatureStatusById() {
        final long validFeatureStatusID = 1;
        final long invalidFeatureStatusID = -1;
        final long nonExistentFeatureStatusID = Integer.MAX_VALUE;

        Mockito.doNothing().when(featureStatusRepository).deleteById(validFeatureStatusID);
        Mockito.doNothing().when(featureStatusRepository).deleteById(invalidFeatureStatusID);
        Mockito.doNothing().when(featureStatusRepository).deleteById(nonExistentFeatureStatusID);

        // nothing really to test here
    }
}