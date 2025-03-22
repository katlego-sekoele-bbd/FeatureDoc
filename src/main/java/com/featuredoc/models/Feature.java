
package com.featuredoc.models;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long featureID;
    private @NonNull Long createdBy;
    @Column(updatable = false, insertable = false)
    private Timestamp createdAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Feature feature = (Feature) o;
        return Objects.equals(getFeatureID(), feature.getFeatureID()) && Objects.equals(getCreatedBy(), feature.getCreatedBy()) && Objects.equals(getCreatedAt(), feature.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFeatureID(), getCreatedBy(), getCreatedAt());
    }
}
