package com.upb.okrbackend.controllers;


import com.upb.okrbackend.models.User;
import com.upb.okrbackend.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import java.util.concurrent.ExecutionException;

@RequestMapping("/api/User")
@RestController
@EnableWebMvc
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping(value = "/get")
    public User getUser(@RequestParam String id) throws ExecutionException, InterruptedException {
        return userService.getUser(id);
    }
    @PostMapping(value = "/create")
    public String createUser(@RequestBody User user) throws ExecutionException, InterruptedException {
        return userService.createUser(user);
    }
    @PutMapping(value = "/update")
    public String updateUser(@RequestParam User user) throws ExecutionException, InterruptedException {
        return userService.updateUser(user);
    }
    @PutMapping(value = "/delete")
    public String deleteUser(@RequestParam String id) throws ExecutionException, InterruptedException {
        return userService.deleteUser(id);
    }
}
