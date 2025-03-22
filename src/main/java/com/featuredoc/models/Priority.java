package com.featuredoc.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Priority priority = (Priority) o;
        return Objects.equals(getPriorityID(), priority.getPriorityID()) && Objects.equals(getDescription(), priority.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPriorityID(), getDescription());
    }
}
