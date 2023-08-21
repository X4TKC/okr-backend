package com.upb.okrbackend.entities;
public class CheckEntity {
    private String id;
    private String userId;
    private String objectiveId;
    private String checkDate;
    private boolean checked;
    private long measurementValue;
    private boolean isIncreasing;

    public CheckEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getObjectiveId() {
        return objectiveId;
    }

    public void setObjectiveId(String objectiveId) {
        this.objectiveId = objectiveId;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public long getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(long measurementValue) {
        this.measurementValue = measurementValue;
    }

    public boolean isIncreasing() {
        return isIncreasing;
    }

    public void setIncreasing(boolean increasing) {
        isIncreasing = increasing;
    }
}
