package com.featuredoc.models;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class FeatureVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer featureVersionID;

    private @NonNull Integer updateBy;
    private @NonNull Integer featureID;
    private @NonNull Integer featureStatusID;
    private @NonNull Integer priorityID;
    private @NonNull Integer assignedTo;
    private @NonNull String name;
    private @NonNull String shortDescription;

    @Column(updatable = false, insertable = false)
    private Timestamp updatedDate;

    private Timestamp deletedDate;

    private @NonNull String URL;

    public Integer getFeatureVersionID() {
        return featureVersionID;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public Integer getFeatureID() {
        return featureID;
    }

    public Integer getFeatureStatusID() {
        return featureStatusID;
    }

    public Integer getPriorityID() {
        return priorityID;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public Timestamp getDeletedDate() {
        return deletedDate;
    }

    public String getURL() {
        return URL;
    }

}
