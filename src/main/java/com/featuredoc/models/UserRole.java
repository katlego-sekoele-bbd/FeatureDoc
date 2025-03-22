package com.featuredoc.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(getId(), userRole.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}