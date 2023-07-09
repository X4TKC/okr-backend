package com.upb.okrbackend.models;



import com.google.cloud.Timestamp;
import com.upb.okrbackend.entities.ObjectiveEntity;

import java.util.Date;
import java.util.List;

public class User {

    private String id;
    private String name;
    private String password;
    private String email;
    private List<Objective> objectiveList;
    private Timestamp creationdate;

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

    public Timestamp getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Timestamp creationdate) {
        this.creationdate = creationdate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public List<Objective> getObjectiveList() {
        return objectiveList;
    }

    public void setObjectiveList(List<Objective> objectiveList) {
        this.objectiveList = objectiveList;
    }
}
