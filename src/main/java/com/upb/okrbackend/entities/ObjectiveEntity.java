package com.upb.okrbackend.entities;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.upb.okrbackend.models.KeyResult;

import java.util.List;

public class ObjectiveEntity {
    private String id;
    private String name;
    private List<DocumentReference> keyResultList;
    private String dateStart;
    private String dateEnd;
    private String userId;

    private boolean enable;
    private String type;

    public ObjectiveEntity() {
    }
    public ObjectiveEntity(String id, String name, List<DocumentReference> keyResultList, String dateStart, String dateEnd, String userId) {
        this.id = id;
        this.name = name;
        this.keyResultList = keyResultList;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.userId = userId;
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

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public List<DocumentReference> getKeyResultList() {
        return keyResultList;
    }

    public void setKeyResultList(List<DocumentReference> keyResultList) {
        this.keyResultList = keyResultList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
