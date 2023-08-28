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
    private boolean enable;
    private String type;
    private String state;
    private int progressTracker;
    private List<Check> checkList;
    public Objective() {
    }
    public Objective(String id, String name, List<KeyResult> keyResultList, String dateStart, String dateEnd, String userId, List<Check> checkList, String type, String state, int progressTracker, boolean enable) {
        this.id = id;
        this.name = name;
        this.keyResultList = keyResultList;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.userId = userId;
        this.checkList=checkList;
        this.state=state;
        this.type=type;
        this.progressTracker=progressTracker;
        this.enable=enable;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getProgressTracker() {
        return progressTracker;
    }

    public void setProgressTracker(int progressTracker) {
        this.progressTracker = progressTracker;
    }
    public List<Check> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<Check> checkList) {
        this.checkList = checkList;
    }
}
