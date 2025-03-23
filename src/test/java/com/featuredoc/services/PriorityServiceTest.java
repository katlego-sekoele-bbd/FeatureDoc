package com.featuredoc.services;

import com.featuredoc.models.Priority;
import com.featuredoc.repository.FeatureViewRepository;
import com.featuredoc.repository.PriorityRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
class PriorityServiceTest {

    @Mock
    private PriorityRepository priorityRepository;

    @InjectMocks
    private PriorityService priorityService;

    @Test
    public void getAllPrioritiesBestCase() {
        List<Priority> priorities = List.of(
                new Priority(1, "1"),
                new Priority(2, "2"),
                new Priority(2, "2")
        );

        when(priorityRepository.findAll())
                .thenReturn(priorities);

        List<Priority> actual = priorityService.getAllPriorities();

        assertEquals(priorities, actual);
    }

    @Test
    public void getAllPrioritiesEmptyDB() {
        List<Priority> priorities = List.of();

        when(priorityRepository.findAll())
                .thenReturn(priorities);

        List<Priority> actual = priorityService.getAllPriorities();

        assertEquals(priorities, actual);
    }

    @Test
    public void createPriorityBestCase() {
        Priority priority = new Priority(1, "1");

        when(priorityRepository.save(priority))
                .thenReturn(priority);

        Priority actual = priorityService.createPriority(priority);

        assertEquals(priority, actual);
    }

    @Test
    public void createPriorityNullPriority() {
        Priority priority = null;

        when(priorityRepository.save(priority))
                .thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> priorityService.createPriority(priority));
    }

    @Test
    public void createPriorityAlreadyExists() {
        Priority priority = new Priority("1");

        when(priorityRepository.save(priority))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> priorityService.createPriority(new Priority("1")));
    }

    @Test
    public void getPriorityByIDBestCase() {
        Priority priority = new Priority(1, "1");

        when(priorityRepository.findById(Long.valueOf(priority.getPriorityID())))
                .thenReturn(Optional.of(priority));

        Optional<Priority> actual = priorityService.getPriorityById(priority.getPriorityID());

        assertEquals(Optional.of(priority), actual);
    }

    @Test
    public void getPriorityByIDNotExists() {
        Priority priority = new Priority(1, "1");

        when(priorityRepository.findById(-1L))
                .thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> priorityService.getPriorityById(-1));
    }

    @Test
    public void deletePriorityByIDBestCase() {
        Priority priority = new Priority(1, "1");

        doNothing().when(priorityRepository).deleteById(1L);
        when(priorityRepository.existsById(1L))
                .thenReturn(true);

        priorityService.deletePriorityById(1L);
        verify(priorityRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deletePriorityByIDNotExists() {
        priorityService.deletePriorityById(-1);
        verify(priorityRepository, times(1)).deleteById(-1L);
    }

}