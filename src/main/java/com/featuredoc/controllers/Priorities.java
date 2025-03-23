package com.featuredoc.controllers;

import com.featuredoc.models.Priority;
import com.featuredoc.repository.PriorityRepository;
import com.featuredoc.services.PriorityService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/priorities")
@Validated
public class Priorities {

    @Autowired
    PriorityService priorityService;

    @Autowired
    PriorityRepository priorityRepository;

    @GetMapping(value = {"/", ""})
    public List<Priority> getAllPriorities() {
        return priorityService.getAllPriorities();
    }

    @PostMapping(value = {"/", ""})
    public Priority createPriority(@RequestBody Priority priority) {
        return priorityService.createPriority(priority);
    }

    @GetMapping("/{priorityID}")
    public Priority getPriorityById(
            @PathVariable("priorityID")
            @Min(value = 1, message = "priorityID must be a positive integer")
            long priorityID)
    {
        return priorityService.getPriorityById(priorityID)
                .orElse(new Priority());
    }

    @DeleteMapping("/{priorityID}")
    public ResponseEntity<Void> deletePriorityById(
            @PathVariable("priorityID") @Min(value = 1, message = "priorityID must be a positive integer") long priorityID) {
        if (!priorityRepository.existsById(priorityID)) {
            throw new DataIntegrityViolationException(("priorityID " + priorityID + " does not exist in the database"));
        } else {
            priorityService.deletePriorityById(priorityID);
            return ResponseEntity.noContent().build();
        }
    }

}
