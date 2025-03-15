package com.featuredoc.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
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
}
