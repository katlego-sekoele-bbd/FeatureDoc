package com.featuredoc.controllers;

import com.featuredoc.exceptions.ResourceNotFoundException;
import com.featuredoc.models.Priority;
import com.featuredoc.services.PriorityService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/priorities")
@CrossOrigin
@Validated
public class Priorities {

    @Autowired
    PriorityService priorityService;

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
                .orElseThrow(() -> new ResourceNotFoundException("Priority", "priorityID", priorityID));
    }

    @DeleteMapping("/{priorityID}")
    public ResponseEntity<Object> deletePriorityById(
            @PathVariable("priorityID")
            @Min(value = 1, message = "priorityID must be a positive integer")
            long priorityID
    ) {
        priorityService.deletePriorityById(priorityID);
        return ResponseEntity.noContent().build();
    }

}
