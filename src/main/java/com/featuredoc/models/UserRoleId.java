package com.featuredoc.models;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.*;

@Embeddable // can be embedded into an entity as a composite key.
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleId implements Serializable {

    private Long roleID;
    private Long userID;

    public Long getUserID() {
        return userID;
    }

    public Long getRoleID() {
        return roleID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleId that = (UserRoleId) o;
        return Objects.equals(getRoleID(), that.getRoleID()) && Objects.equals(getUserID(), that.getUserID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleID(), getUserID());
    }
}

