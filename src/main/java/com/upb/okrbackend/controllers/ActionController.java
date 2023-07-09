package com.upb.okrbackend.controllers;

import com.upb.okrbackend.models.Action;
import com.upb.okrbackend.models.Measurement;
import com.upb.okrbackend.models.User;
import com.upb.okrbackend.service.ActionService;
import com.upb.okrbackend.service.MeasurementService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.concurrent.ExecutionException;
@RequestMapping("/api/Action")
@RestController
@EnableWebMvc
public class ActionController {
    private ActionService actionService;

    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }
    @GetMapping(value = "/get")
    public Action getAction(@RequestParam String id) throws ExecutionException, InterruptedException {
        return actionService.getAction(id);
    }
    @PostMapping(value = "/create")
    public String createAction(@RequestBody Action action) throws ExecutionException, InterruptedException {
        return actionService.createAction(action);
    }
    @PutMapping(value = "/update")
    public String updateAction(@RequestBody Action action) throws ExecutionException, InterruptedException {
        return actionService.updateAction(action);
    }
    @PutMapping(value = "/delete")
    public String deleteAction(@RequestParam String id) {
        return actionService.deleteAction(id);
    }
}
