package com.featuredoc.services;

import com.featuredoc.models.FeatureView;
import com.featuredoc.repository.FeatureVersionRepository;
import com.featuredoc.repository.FeatureViewRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
class FeatureViewServiceTest {

    @Mock
    private FeatureViewRepository featureViewRepository;

    @InjectMocks
    private FeatureViewService featureViewService;

    @Test
    public void getLatestFeatureVersionByFeatureIdBestCase() {
        FeatureView featureView = new FeatureView(1, "name", "test@test.com", LocalDateTime.now(), 1, "test@test.com", "status", "priority", "test@test.com", "name", "description", "url", LocalDateTime.now(), null);

        when(featureViewRepository.findLatestVersionByFeatureId(featureView.getFeatureID()))
                .thenReturn(Optional.of(featureView));

        Optional<FeatureView> actual = featureViewService.getLatestFeatureVersionByFeatureId(featureView.getFeatureID());

        assertEquals(Optional.of(featureView), actual);
    }

    @Test
    public void getLatestFeatureVersionByFeatureIdNoLatest() {
        when(featureViewRepository.findLatestVersionByFeatureId(-1))
                .thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> featureViewService.getLatestFeatureVersionByFeatureId(-1));
    }

    @Test
    public void getAllVersionsByFeatureIdBestCase() {
        List<FeatureView> featureViews = List.of(
                new FeatureView(1, "name", "test@test.com", LocalDateTime.now(), 1, "test@test.com", "status", "priority", "test@test.com", "name", "description", "url", LocalDateTime.now(), null),
                new FeatureView(2, "name 2", "test@test.com", LocalDateTime.now(), 1, "test@test.com", "status", "priority", "test@test.com", "name", "description", "url", LocalDateTime.now(), null),
                new FeatureView(3, "name 3", "test@test.com", LocalDateTime.now(), 1, "test@test.com", "status", "priority", "test@test.com", "name", "description", "url", LocalDateTime.now(), null)
            );

        when(featureViewRepository.findAllVersionsByFeatureId(featureViews.get(0).getFeatureID()))
                .thenReturn(featureViews);

        List<FeatureView> actual = featureViewService.getAllVersionsByFeatureId(featureViews.get(0).getFeatureID());

        assertEquals(featureViews, actual);
    }

    @Test
    public void getAllVersionsByFeatureIdNoVersions() {
        List<FeatureView> featureViews = List.of();

        when(featureViewRepository.findAllVersionsByFeatureId(1))
                .thenReturn(featureViews);

        List<FeatureView> actual = featureViewService.getAllVersionsByFeatureId(1);

        assertEquals(featureViews, actual);
    }

    @Test
    public void getAllVersionsByFeatureIdNoFeature() {
        when(featureViewRepository.findAllVersionsByFeatureId(-1))
                .thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> featureViewService.getAllVersionsByFeatureId(-1));
    }

}