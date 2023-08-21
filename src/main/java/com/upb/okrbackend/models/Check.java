package com.upb.okrbackend.models;

public class Check {
    private String id;
    private String objectiveId;
    private String keyResultId;
    private String checkDate;
    private boolean checked;
    private long value;


    public Check() {
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getObjectiveId() {
        return objectiveId;
    }
    public void setObjectiveId(String objectiveId) {
        this.objectiveId = objectiveId;
    }
    public String getKeyResultId() {
        return keyResultId;
    }
    public void setKeyResultId(String keyResultId) {
        this.keyResultId = keyResultId;
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

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

}
