package com.featuredoc.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Priority {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer priorityID;
    private @NonNull String description;

}
