package com.upb.okrbackend.controllers;

import com.upb.okrbackend.models.KeyResult;
import com.upb.okrbackend.repositories.KeyResultRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Collection;

@RequestMapping("/api/keyResult")
@RestController
@EnableWebMvc
public class KeyResultController {
    final KeyResultRepository keyResultRepository;

    public KeyResultController(KeyResultRepository keyResultRepository) {
        this.keyResultRepository = keyResultRepository;
    }
    @GetMapping(value = "/get")
    public Collection<KeyResult> getAllKeyResult() {
        return keyResultRepository.findAll();
    }
}
