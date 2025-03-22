package com.featuredoc.services;

import com.featuredoc.models.FeatureVersion;
import com.featuredoc.models.FeatureView;
import com.featuredoc.repository.FeatureStatusRepository;
import com.featuredoc.repository.FeatureVersionRepository;
import com.featuredoc.repository.FeatureViewRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
class FeatureVersionServiceTest {

    @Mock
    private FeatureVersionRepository featureVersionRepository;

    @InjectMocks
    private FeatureVersionService featureVersionService;

    @Test
    void getLatestFeatureVersionByFeatureIdBestCase() {
        FeatureVersion featureVersion = new FeatureVersion(1L, 1L, 1L, 1, 1, 1L, "name", "description", Timestamp.from(Instant.now()), null, "url");

        when(featureVersionRepository.getLatestVersionByFeatureId(featureVersion.getFeatureID()))
                .thenReturn(Optional.of(featureVersion));

        Optional<FeatureVersion> actual = featureVersionService.getLatestFeatureVersionByFeatureId(featureVersion.getFeatureID());

        assertEquals(Optional.of(featureVersion), actual);
    }

    @Test
    void getLatestFeatureVersionByFeatureIdNoLatest() {
        when(featureVersionRepository.getLatestVersionByFeatureId(-1L))
                .thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> featureVersionService.getLatestFeatureVersionByFeatureId(-1L));
    }
}