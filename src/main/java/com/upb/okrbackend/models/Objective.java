package com.upb.okrbackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;
@Entity
public class Objective {
    @Id
    private Long id;
    private String title;
    private String description;
    //private List<KeyResult> keyResultList;


    public Objective(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Objective() {

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

//    public KeyResult addKeyResult(String title, String description, float target, float current){
//        this.keyResultList.add(new KeyResult(title,description,target,current));
//        return keyResultList.get(keyResultList.size()-1);
//    }
//    public void removeKeyResult(KeyResult keyResult){
//        this.keyResultList.remove(keyResult);
//    }
//
//    public List<KeyResult> getKeyResultList() {
//        return keyResultList;
//    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
