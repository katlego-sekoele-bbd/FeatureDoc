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
    private Long featureVersionID;

    private @NonNull Long updateBy;
    private @NonNull Long featureID;
    private Integer featureStatusID;
    private Integer priorityID;
    private Long assignedTo;
    private @NonNull String name;
    private String shortDescription;

    @Column(updatable = false, insertable = false)
    private Timestamp updatedDate;

    public FeatureVersion(Long updateBy, Long featureID, Integer featureStatusID, Integer priorityID,
            Long assignedTo, String name, String shortDescription, String URL) {
        this.updateBy = updateBy;
        this.featureID = featureID;
        this.featureStatusID = featureStatusID;
        this.priorityID = priorityID;
        this.assignedTo = assignedTo;
        this.name = name;
        this.shortDescription = shortDescription;
        this.URL = URL;
    }

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

    private String URL;

}
