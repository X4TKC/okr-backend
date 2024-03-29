package com.upb.okrbackend.entities;

import com.google.cloud.firestore.DocumentReference;
import com.upb.okrbackend.models.Check;

import java.util.List;

public class KeyResultEntity {
    private String id;
    private String description;
    private String objectiveId;
    private String action;
    private String measurement;
    private boolean check;
    private List<DocumentReference> checkList;
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

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }
    public boolean getCheck() {
        return check;
    }

    public List<DocumentReference> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<DocumentReference> checkList) {
        this.checkList = checkList;
    }
}
