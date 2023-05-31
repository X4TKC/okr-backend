package com.upb.okrbackend.controllers;

import com.upb.okrbackend.models.Objective;
import com.upb.okrbackend.repositories.ObjectiveRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Collection;

@RequestMapping("/api/Objective")
@RestController
@EnableWebMvc
public class ObjectiveController {
    final ObjectiveRepository objectiveRepository;

    public ObjectiveController(ObjectiveRepository objectiveRepository) {
        this.objectiveRepository = objectiveRepository;
    }
    @GetMapping(value = "/get")
    public Collection<Objective> getAllKeyResult() {
        return objectiveRepository.findAll();
    }
}
