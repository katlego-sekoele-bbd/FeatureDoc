package com.featuredoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FeatureRequest {
    private Integer createdBy;
    private Integer featureID;
    private Integer updatedBy;
    private Integer featureStatusID;
    private Integer priorityID;
    private Integer assignedTo;
    private String name;

    public Integer getCreatedBy() {
        return createdBy;
    }

    public Integer getFeatureID() {
        return featureID;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
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

    public String getURL() {
        return URL;
    }

    private String shortDescription;
    private String URL;


}
