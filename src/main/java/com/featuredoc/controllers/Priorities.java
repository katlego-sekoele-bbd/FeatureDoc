package com.featuredoc.controllers;

import com.featuredoc.models.Priority;
import com.featuredoc.services.PriorityService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Object> deletePriorityById(
            @PathVariable("priorityID")
            @Min(value = 1, message = "priorityID must be a positive integer")
            long priorityID
    ) {
        try {
            priorityService.deletePriorityById(priorityID);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete Failed");
        }

    }

}
