package com.featuredoc.services;

import com.featuredoc.exceptions.ResourceNotFoundException;
import com.featuredoc.models.FeatureStatus;
import com.featuredoc.repository.FeatureStatusRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.verification.VerificationMode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
class FeatureStatusServiceTest {

    @Mock
    private FeatureStatusRepository featureStatusRepository;

    @InjectMocks
    private FeatureStatusService featureStatusService;

    @Test
    public void getAllFeatureStatusesBestCase() {
        final List<FeatureStatus> featureStatuses = List.of(
                new FeatureStatus(1, "1"),
                new FeatureStatus(2, "2"),
                new FeatureStatus(3, "3")
        );

        when(featureStatusRepository.findAll())
                .thenReturn(featureStatuses);

        List<FeatureStatus> actual = featureStatusService.getAllFeatureStatuses();

        assertEquals(featureStatuses, actual);
    }

    @Test
    public void getAllFeatureStatusesNoStatuses() {
        final List<FeatureStatus> featureStatuses = List.of();

        when(featureStatusRepository.findAll())
                .thenReturn(List.of());

        List<FeatureStatus> actual = featureStatusService.getAllFeatureStatuses();

        assertEquals(featureStatuses, actual);
    }

    @Test
    public void createFeatureStatusBestCase() {
        final FeatureStatus featureStatus = new FeatureStatus(1, "1");

        when(featureStatusRepository.save(featureStatus))
                .thenReturn(featureStatus);

        FeatureStatus actual = featureStatusService.createFeatureStatus(featureStatus);

        assertEquals(featureStatus, actual);
    }

    @Test
    public void createFeatureStatusNoStatus() {
        final FeatureStatus featureStatus = null;

        when(featureStatusRepository.save(featureStatus))
                .thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> featureStatusService.createFeatureStatus(featureStatus));
    }

    @Test
    public void createFeatureStatusAlreadyExists() {
        List<FeatureStatus> featureStatuses = List.of(
                new FeatureStatus("status")
        );

        when(featureStatusRepository.save(new FeatureStatus("status")))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> featureStatusService.createFeatureStatus(new FeatureStatus("status")));
    }

    @Test
    public void getFeatureStatusByIDBestCase() {
        final Optional<FeatureStatus> featureStatus = Optional.of(new FeatureStatus(1, "1"));

        when(featureStatusRepository.findById(Long.valueOf(featureStatus.get().getFeatureStatusID())))
                .thenReturn(featureStatus);

        Optional<FeatureStatus> actual = featureStatusService.getFeatureStatusById(featureStatus.get().getFeatureStatusID());

        assertEquals(featureStatus, actual);
    }

    @Test
    public void getFeatureStatusByIDNoFeatureStatus() {
        when(featureStatusRepository.findById(-1L))
                .thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> featureStatusService.getFeatureStatusById(-1L));
    }

    @Test
    public void deleteFeatureStatusByIDBestCase() {
        final Optional<FeatureStatus> featureStatus = Optional.of(new FeatureStatus(1, "1"));

        when(featureStatusRepository.existsById(Long.valueOf(featureStatus.get().getFeatureStatusID())))
                .thenReturn(true);
        doNothing().when(featureStatusRepository).deleteById(Long.valueOf(featureStatus.get().getFeatureStatusID()));

        featureStatusService.deleteFeatureStatusById(Long.valueOf(featureStatus.get().getFeatureStatusID()));
        verify(featureStatusRepository, times(1)).deleteById(Long.valueOf(featureStatus.get().getFeatureStatusID()));
    }

    @Test
    public void deleteFeatureStatusByIDNotExist() {
        featureStatusService.deleteFeatureStatusById(-1L);
        verify(featureStatusRepository, times(1)).deleteById(-1L);
    }
}