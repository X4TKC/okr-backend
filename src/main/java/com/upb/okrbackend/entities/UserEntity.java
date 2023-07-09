package com.upb.okrbackend.entities;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.upb.okrbackend.models.Objective;

import javax.print.Doc;
import java.util.Date;

import java.util.List;
import java.util.Map;


public class UserEntity {
    private String id;
    private String name;
    private String password;
    private String email;
    private List<DocumentReference> objectiveList;
    private Timestamp creationdate;

    public UserEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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


    public List<DocumentReference> getObjectiveList() {
        return objectiveList;
    }

    public void setObjectiveList(List<DocumentReference> objectiveList) {
        this.objectiveList = objectiveList;
    }

    public Timestamp getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Timestamp creationdate) {
        this.creationdate = creationdate;
    }
}
