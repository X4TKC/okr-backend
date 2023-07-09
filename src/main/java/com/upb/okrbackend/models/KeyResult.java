package com.upb.okrbackend.models;


import com.google.cloud.firestore.DocumentReference;

import java.util.List;

public class KeyResult {
    private String id;
    private String description;
    private String objectiveId;
    private String action;
    private String measurement;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectiveId() {
        return objectiveId;
    }

    public void setObjectiveId(String objectiveId) {
        this.objectiveId = objectiveId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }
}
