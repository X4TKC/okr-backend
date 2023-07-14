package com.upb.okrbackend.controllers;


import com.upb.okrbackend.models.KeyResult;
import com.upb.okrbackend.models.Objective;
import com.upb.okrbackend.models.User;
import com.upb.okrbackend.service.KeyResultService;
import com.upb.okrbackend.service.ObjectiveService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.concurrent.ExecutionException;


@RequestMapping("/api/KeyResult")
@RestController
@EnableWebMvc
@CrossOrigin("*")
public class KeyResultController {
    private KeyResultService keyResultService;

    public KeyResultController(KeyResultService keyResultService){
        this.keyResultService = keyResultService;
    }
    @GetMapping(value = "/get")
    public KeyResult getKeyResult(@RequestParam String id) throws ExecutionException, InterruptedException {
        return keyResultService.getKeyResult(id);
    }
    @GetMapping(value = "/getAllFrom")
    public List<KeyResult> getAllKeyResultsFromObjective(@RequestParam String id) throws ExecutionException, InterruptedException {
        return keyResultService.getAllKeyResultsFromObjective(id);
    }
    @PostMapping(value = "/create")
    public String createKeyResult(@RequestBody KeyResult keyResult) throws ExecutionException, InterruptedException {
        return keyResultService.createKeyResult(keyResult);
    }
    @PutMapping(value = "/update")
    public String updateKeyResult(@RequestBody KeyResult keyResult) throws ExecutionException, InterruptedException {
        return keyResultService.updateKeyResult(keyResult);
    }
    @DeleteMapping(value = "/delete")
    public String deleteKeyResult(@RequestParam String id) {
        return keyResultService.deleteKeyResult(id);
    }

}
