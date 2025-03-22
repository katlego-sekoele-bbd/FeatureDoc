package com.featuredoc.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import java.util.Objects;

@Entity
@AllArgsConstructor
public class User {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    private String name;

    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User() {
    }


    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getUserID(), user.getUserID()) && Objects.equals(getName(), user.getName()) && Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserID(), getName(), getEmail());
    }
}
