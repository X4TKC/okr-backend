package com.upb.okrbackend.entities;

import com.google.cloud.firestore.DocumentReference;
import com.upb.okrbackend.models.Measurement;

import java.util.List;

public class ActionEntity {
    private String id;
    private String description;
    private String keyId;
    private List<DocumentReference> measurementList;

    public ActionEntity() {
    }

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

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public List<DocumentReference> getMeasurementList() {
        return measurementList;
    }

    public void setMeasurementList(List<DocumentReference> measurementList) {
        this.measurementList = measurementList;
    }
}
