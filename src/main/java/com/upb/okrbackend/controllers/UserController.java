package com.upb.okrbackend.controllers;



import com.upb.okrbackend.OkrExceptionErrors;
import com.upb.okrbackend.models.User;
import com.upb.okrbackend.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import java.util.concurrent.ExecutionException;

@RequestMapping("/api/User")
@RestController
@EnableWebMvc
@CrossOrigin("*")
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
    public String createUser(@RequestBody User user) throws ExecutionException, InterruptedException, OkrExceptionErrors {
        return userService.createUser(user);
    }
    @PutMapping(value = "/update")
    public String updateUser(@RequestBody User user) throws ExecutionException, InterruptedException {
        return userService.updateUser(user);
    }
    @DeleteMapping(value = "/delete")
    public String deleteUser(@RequestParam String id) {
        return userService.deleteUser(id);
    }
}
