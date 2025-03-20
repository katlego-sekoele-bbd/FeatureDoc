package com.featuredoc.models;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class FeatureVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long featureVersionID;

    private @NonNull Long updateBy;
    private @NonNull Long featureID;
    private @NonNull Integer featureStatusID;
    private @NonNull Integer priorityID;
    private @NonNull Long assignedTo;
    private @NonNull String name;
    private @NonNull String shortDescription;

    @Column(updatable = false, insertable = false)
    private Timestamp updatedDate;

    public Long getFeatureVersionID() {
        return featureVersionID;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public Long getFeatureID() {
        return featureID;
    }

    public Integer getFeatureStatusID() {
        return featureStatusID;
    }

    public Integer getPriorityID() {
        return priorityID;
    }

    public Long getAssignedTo() {
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

    private Timestamp deletedDate;

    private @NonNull String URL;



}
