package com.featuredoc.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureView {

    @Id
    private Integer featureVersionID; 
    
    private String featureName; 
    private String createdBy; 
    private LocalDateTime createdAt; 
    private Integer featureID;
    
    private String updateBy; 
    private String featureStatus; 
    private String priority; 
    private String assignedTo; 
    
    private String name;
    private String shortDescription; 
    private String URL;
    private LocalDateTime updatedDate; 
    private LocalDateTime deletedDate;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FeatureView that = (FeatureView) o;
        return Objects.equals(getFeatureVersionID(), that.getFeatureVersionID()) && Objects.equals(getFeatureName(), that.getFeatureName()) && Objects.equals(getCreatedBy(), that.getCreatedBy()) && Objects.equals(getCreatedAt(), that.getCreatedAt()) && Objects.equals(getFeatureID(), that.getFeatureID()) && Objects.equals(getUpdateBy(), that.getUpdateBy()) && Objects.equals(getFeatureStatus(), that.getFeatureStatus()) && Objects.equals(getPriority(), that.getPriority()) && Objects.equals(getAssignedTo(), that.getAssignedTo()) && Objects.equals(getName(), that.getName()) && Objects.equals(getShortDescription(), that.getShortDescription()) && Objects.equals(getURL(), that.getURL()) && Objects.equals(getUpdatedDate(), that.getUpdatedDate()) && Objects.equals(getDeletedDate(), that.getDeletedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFeatureVersionID(), getFeatureName(), getCreatedBy(), getCreatedAt(), getFeatureID(), getUpdateBy(), getFeatureStatus(), getPriority(), getAssignedTo(), getName(), getShortDescription(), getURL(), getUpdatedDate(), getDeletedDate());
    }
}
