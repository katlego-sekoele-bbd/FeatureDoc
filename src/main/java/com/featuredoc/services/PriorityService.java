package com.featuredoc.services;

import com.featuredoc.exceptions.ResourceNotFoundException;
import com.featuredoc.models.Priority;
import com.featuredoc.models.Role;
import com.featuredoc.repository.PriorityRepository;
import com.featuredoc.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PriorityService {

    @Autowired
    PriorityRepository priorityRepository;

    public List<Priority> getAllPriorities() {
        return priorityRepository.findAll();
    }

    public Priority createPriority(Priority priority) {
        return priorityRepository.save(priority);
    }

    public Optional<Priority> getPriorityById(long priorityID) {
        return priorityRepository.findById(priorityID);
    }

    public void deletePriorityById(long priorityID) {
        if (priorityRepository.existsById(priorityID)) {
            priorityRepository.deleteById(priorityID);
        } else {
            throw new ResourceNotFoundException("Priority", "priorityID", priorityID);
        }
    }

}
