package com.featuredoc.models;

import java.io.Serializable;
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

}

