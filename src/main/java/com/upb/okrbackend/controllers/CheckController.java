package com.upb.okrbackend.controllers;

import com.upb.okrbackend.models.Check;
import com.upb.okrbackend.service.CheckService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RequestMapping("/api/Check")
@RestController
@EnableWebMvc
@CrossOrigin("*")
public class CheckController {
    private CheckService checkService;

    public CheckController(CheckService checkService) {
        this.checkService = checkService;
    }
    @GetMapping(value = "/get")
    public Check getCheck(@RequestParam String id) throws ExecutionException, InterruptedException {
        return checkService.getCheck(id);
    }
    @GetMapping(value = "/getAllFrom")
    public List<Check> getAllChecksFromKeyResult(@RequestParam String id) throws ExecutionException, InterruptedException {
        return checkService.getAllChecksFromKeyResult(id);
    }
    @PostMapping(value = "/create")
    public String createCheck(@RequestBody Check check) throws ExecutionException, InterruptedException {
        return checkService.createCheck(check);
    }
    @PutMapping(value = "/update")
    public String updateCheck(@RequestBody Check check) throws ExecutionException, InterruptedException {
        return checkService.updateCheck(check);
    }
    @DeleteMapping(value = "/delete")
    public String deleteCheck(@RequestParam String id) {
        return checkService.deleteCheck(id);
    }
    @PutMapping(value = "/check")
    public String checkIndividualCheck(@RequestParam String id) throws ExecutionException, InterruptedException {
        return checkService.checkIndividualCheck(id);
    }
    @PutMapping(value = "/unCheck")
    public String unCheckKeyResult(@RequestParam String id) throws ExecutionException, InterruptedException {
        return checkService.unCheckIndividualCheck(id);
    }
}
