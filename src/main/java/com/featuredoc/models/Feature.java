
package com.featuredoc.models;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer featureID;
    private @NonNull Integer createdBy;
    @Column(updatable = false, insertable = false)
    private Timestamp createdAt;
}
