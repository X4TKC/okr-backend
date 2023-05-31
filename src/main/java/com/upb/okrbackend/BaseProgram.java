package com.upb.okrbackend;

import com.upb.okrbackend.interfaces.BaseProgramInterface;
import com.upb.okrbackend.models.KeyResult;
import com.upb.okrbackend.models.Objective;

import java.util.ArrayList;
import java.util.List;

public class BaseProgram implements BaseProgramInterface {
    private List<Objective> objectives;

    public BaseProgram() {
        objectives= new ArrayList<>();
    }

    @Override
    public void addObjective(String title, String description) {
        objectives.add(new Objective(title, description));
    }

    @Override
    public void removeObjective(Objective objective) {
        objectives.remove(objective);
    }

    @Override
    public List<Objective> getObjectives() {
        return objectives;
    }

//    @Override
//    public KeyResult addKeyResult(Objective objective, String title, String description, float target, float current) {
//        objective.addKeyResult(title, description, target, current);
//        return objective.getKeyResultList().get(objective.getKeyResultList().size()-1);
//    }
//
//    @Override
//    public void RemoveKeyResult(Objective objective, KeyResult keyResult) {
//        objective.removeKeyResult(keyResult);
//    }

}
