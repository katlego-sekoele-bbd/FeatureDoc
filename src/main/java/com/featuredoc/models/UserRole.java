package com.featuredoc.models;
import lombok.Data;
import jakarta.persistence.*;
@Data
@Entity
public class UserRole {
    private @Id @GeneratedValue Integer roleID;
    private Integer userID;
}
