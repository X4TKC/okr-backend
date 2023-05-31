package com.upb.okrbackend.controllers;


import com.upb.okrbackend.models.User;
import com.upb.okrbackend.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Collection;

@RequestMapping("/api/User")
@RestController
@EnableWebMvc
public class UserController {
    final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @GetMapping(value = "/get")
    public Collection<User> getAllKeyResult() {
        return userRepository.findAll();
    }
}
