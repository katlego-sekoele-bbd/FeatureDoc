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
public class FeatureStatus {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer featureStatusID;
    private @NonNull String description;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FeatureStatus that = (FeatureStatus) o;
        return Objects.equals(getFeatureStatusID(), that.getFeatureStatusID()) && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFeatureStatusID(), getDescription());
    }
}
