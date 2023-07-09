package com.upb.okrbackend.controllers;

import com.upb.okrbackend.models.Measurement;
import com.upb.okrbackend.models.User;
import com.upb.okrbackend.service.MeasurementService;
import com.upb.okrbackend.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.concurrent.ExecutionException;
@RequestMapping("/api/Measurement")
@RestController
@EnableWebMvc
public class MeasurementController {
    private MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }
    @GetMapping(value = "/get")
    public Measurement getMeasurement(@RequestParam String id) throws ExecutionException, InterruptedException {
        return measurementService.getMeasurement(id);
    }
    @PostMapping(value = "/create")
    public String createMeasurement(@RequestBody Measurement measurement) throws ExecutionException, InterruptedException {
        return measurementService.createMeasurement(measurement);
    }
    @PutMapping(value = "/update")
    public String updateMeasurement(@RequestBody Measurement measurement) throws ExecutionException, InterruptedException {
        return measurementService.updateMeasurement(measurement);
    }
    @PutMapping(value = "/delete")
    public String deleteMeasurement(@RequestParam String id) {
        return measurementService.deleteMeasurement(id);
    }
}
