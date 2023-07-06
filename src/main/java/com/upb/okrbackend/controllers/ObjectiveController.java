package com.upb.okrbackend.controllers;


import com.upb.okrbackend.models.Objective;
import com.upb.okrbackend.models.User;
import com.upb.okrbackend.service.ObjectiveService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.concurrent.ExecutionException;


@RequestMapping("/api/Objective")
@RestController
@EnableWebMvc
public class ObjectiveController {
    private ObjectiveService objectiveService;

    public ObjectiveController(ObjectiveService objectiveService){
        this.objectiveService = objectiveService;
    }
    @GetMapping(value = "/get")
    public Objective getObjective(@RequestParam String id) throws ExecutionException, InterruptedException {
        return objectiveService.getObjective(id);
    }
    @PostMapping(value = "/create")
    public String createObjective(@RequestBody Objective objective) throws ExecutionException, InterruptedException {
        return objectiveService.createObjective(objective);
    }
    @PutMapping(value = "/update")
    public String updateObjective(@RequestParam Objective objective) throws ExecutionException, InterruptedException {
        return objectiveService.updateObjective(objective);
    }
    @PutMapping(value = "/delete")
    public String deleteObjective(@RequestParam String id) throws ExecutionException, InterruptedException {
        return objectiveService.deleteObjective(id);
    }

}
