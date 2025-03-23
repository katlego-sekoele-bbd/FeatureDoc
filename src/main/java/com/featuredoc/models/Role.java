package com.featuredoc.models;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Role {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleID;
    private @NonNull String roleName;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(getRoleID(), role.getRoleID()) && Objects.equals(getRoleName(), role.getRoleName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleID(), getRoleName());
    }
}
