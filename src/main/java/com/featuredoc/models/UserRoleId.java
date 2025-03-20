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

    private Integer roleID;
    private Integer userID;
}