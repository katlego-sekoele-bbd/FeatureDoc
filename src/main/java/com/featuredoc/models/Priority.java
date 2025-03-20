package com.featuredoc.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Priority {
    public Integer getPriorityID() {
        return priorityID;
    }

    public String getDescription() {
        return description;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer priorityID;
    private @NonNull String description;

}
