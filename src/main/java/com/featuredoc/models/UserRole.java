package com.featuredoc.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor 
public class UserRole {

    @EmbeddedId 
    private UserRoleId id;

    public void setId(UserRoleId userRoleId) {
        this.id = userRoleId;
    }

    public UserRoleId getId() {
        return this.id;
    }
}