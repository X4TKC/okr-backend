package com.upb.okrbackend.interfaces;

import com.upb.okrbackend.models.KeyResult;
import com.upb.okrbackend.models.Objective;

import java.util.List;

public interface BaseProgramInterface {
    void addObjective(String title, String description);
    void removeObjective(Objective objective);
    List<Objective> getObjectives();
//    KeyResult addKeyResult(Objective objective, String title, String description, float target, float current);
//    void RemoveKeyResult(Objective objective, KeyResult keyResult);
}
