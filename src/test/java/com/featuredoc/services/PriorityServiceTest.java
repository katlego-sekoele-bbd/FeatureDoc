package com.featuredoc.services;

import com.featuredoc.models.Priority;
import com.featuredoc.repository.PriorityRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
class PriorityServiceTest {

    @Mock
    private PriorityRepository priorityRepository;

    @InjectMocks
    private PriorityService priorityService;

    private final List<Priority> mockPriorities = List.of(
            new Priority(1, "Low"),
            new Priority(2, "Medium"),
            new Priority(3, "High")
    );

    private final Priority mockCreatePriority = new Priority(4, "Critical");

    @Test
    void getAllPriorities() {
        when(priorityRepository.findAll())
                .thenReturn(mockPriorities);

        var actualPriorities = priorityService.getAllPriorities();

        assertEquals(mockPriorities, actualPriorities);
    }

    @Test
    void createPriority() {
        when(priorityRepository.save(mockCreatePriority)).thenReturn(mockCreatePriority);

        var actualCreatedPriority = priorityService.createPriority(mockCreatePriority);

        assertEquals(actualCreatedPriority, mockCreatePriority);
    }

    @Test
    void getPriorityById() {

        final long validPriorityID = 1;
        final long invalidPriorityID = -1;
        final long nonExistentPriorityID = Integer.MAX_VALUE;

        when(priorityRepository.findById(validPriorityID))
                .thenReturn(Optional.ofNullable(mockPriorities.get((int) (validPriorityID - 1))));

        when(priorityRepository.findById(invalidPriorityID))
                .thenReturn(Optional.empty());

        when(priorityRepository.findById(nonExistentPriorityID))
                .thenReturn(Optional.empty());

        var actualValidPriority = priorityService.getPriorityById(validPriorityID);
        var actualInvalidPriority = priorityService.getPriorityById(invalidPriorityID);
        var actualNonExistentPriority = priorityService.getPriorityById(nonExistentPriorityID);

        assertEquals(actualValidPriority.orElse(null), mockPriorities.get((int) (validPriorityID - 1)));
        assertEquals(actualInvalidPriority, Optional.empty());
        assertEquals(actualNonExistentPriority, Optional.empty());

    }

    @Test
    void deletePriorityById() {

        final long validPriorityID = 1;
        final long invalidPriorityID = -1;
        final long nonExistentPriorityID = Integer.MAX_VALUE;

        Mockito.doNothing().when(priorityRepository).deleteById(validPriorityID);
        Mockito.doNothing().when(priorityRepository).deleteById(invalidPriorityID);
        Mockito.doNothing().when(priorityRepository).deleteById(nonExistentPriorityID);

        // nothing really to test here
    }
}