package com.featuredoc.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeatureRequest {
    private Long createdBy;
    private Long featureID;
    private Long updatedBy;
    private Integer featureStatusID;
    private Integer priorityID;
    private Long assignedTo;
    private String name;

    public Long getCreatedBy() {
        return createdBy;
    }

    public Long getFeatureID() {
        return featureID;
    }

    public Long getUpdatedBy() {
        return updatedBy;
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

    public String getURL() {
        return URL;
    }

    private String shortDescription;
    private String URL;


}
