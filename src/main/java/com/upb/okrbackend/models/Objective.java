package com.upb.okrbackend.models;



import com.google.cloud.Timestamp;

import java.util.Date;
import java.util.List;
public class Objective {

    private String id;
    private String name;
    private List<KeyResult> keyResultList;
    private String dateStart;
    private String dateEnd;
    private String userId;



    public Objective() {
    }
    public Objective(String id, String name, List<KeyResult> keyResultList, String dateStart, String dateEnd, String userId) {
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

    public List<KeyResult> getKeyResultList() {
        return keyResultList;
    }

    public void setKeyResultList(List<KeyResult> keyResultList) {
        this.keyResultList = keyResultList;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
