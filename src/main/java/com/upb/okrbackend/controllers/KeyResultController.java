package com.upb.okrbackend.controllers;


import com.upb.okrbackend.models.KeyResult;
import com.upb.okrbackend.models.User;
import com.upb.okrbackend.service.KeyResultService;
import com.upb.okrbackend.service.ObjectiveService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.concurrent.ExecutionException;


@RequestMapping("/api/KeyResult")
@RestController
@EnableWebMvc
public class KeyResultController {
    private KeyResultService keyResultService;

    public KeyResultController(KeyResultService keyResultService){
        this.keyResultService = keyResultService;
    }
    @GetMapping(value = "/get")
    public KeyResult getKeyResult(@RequestParam String id) throws ExecutionException, InterruptedException {
        return keyResultService.getKeyResult(id);
    }
    @PostMapping(value = "/create")
    public String createKeyResult(@RequestBody KeyResult keyResult) throws ExecutionException, InterruptedException {
        return keyResultService.createKeyResult(keyResult);
    }
    @PutMapping(value = "/update")
    public String updateKeyResult(@RequestParam KeyResult keyResult) throws ExecutionException, InterruptedException {
        return keyResultService.updateKeyResult(keyResult);
    }
    @PutMapping(value = "/delete")
    public String deleteKeyResult(@RequestParam String id) throws ExecutionException, InterruptedException {
        return keyResultService.deleteKeyResult(id);
    }

}
