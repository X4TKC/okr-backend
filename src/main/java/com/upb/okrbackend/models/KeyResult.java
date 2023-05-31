package com.upb.okrbackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class KeyResult {
    private String title;
    private String description;
    private float target;
    private float current;
    @Id
    private Long id;

    public KeyResult(String title, String description, float target, float current) {
        this.title = title;
        this.description = description;
        this.target = target;
        this.current = current;
    }

    public KeyResult() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getTarget() {
        return target;
    }

    public void setTarget(float target) {
        this.target = target;
    }

    public float getCurrent() {
        return current;
    }

    public void setCurrent(float current) {
        this.current = current;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
