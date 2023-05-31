package com.upb.okrbackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.List;
@Entity
public class User {
    @Id
    private Long id;
    private String name;
    private String password;
    private String email;
    //private List<Objective> userObjectives;
    private String creationDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public List<Objective> getUserObjectives() {
//        return userObjectives;
//    }
//
//    public void setUserObjectives(List<Objective> userObjectives) {
//        this.userObjectives = userObjectives;
//    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
