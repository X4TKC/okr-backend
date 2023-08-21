package com.upb.okrbackend.controllers;

import com.upb.okrbackend.models.Check;
import com.upb.okrbackend.models.KeyValue;
import com.upb.okrbackend.service.KeyValueService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RequestMapping("/api/KeyValue")
@RestController
@EnableWebMvc
@CrossOrigin("*")
public class KeyValueController {
    private KeyValueService keyValueService;
    public KeyValueController(KeyValueService keyValueService){
        this.keyValueService=keyValueService;
    }
    @GetMapping(value = "/get")
    public KeyValue getCheck(@RequestParam String id) throws ExecutionException, InterruptedException {
        return keyValueService.getKeyValue(id);
    }
    @GetMapping(value = "/getAllFromKey")
    public List<KeyValue> getAllKeyValueByKeyId(@RequestParam String id) throws ExecutionException, InterruptedException {
        return keyValueService.getAllKeyValueByKeyId(id);
    }
    @GetMapping(value = "/getAllFromObj")
    public List<KeyValue> getAllKeyValueByObjectiveId(@RequestParam String id) throws ExecutionException, InterruptedException {
        return keyValueService.getAllKeyValueByObjectiveId(id);
    }
    @PostMapping(value = "/create")
    public String createCheck(@RequestBody KeyValue keyValue) throws ExecutionException, InterruptedException {
        return keyValueService.createKeyValue(keyValue);
    }
    @PutMapping(value = "/update")
    public String updateCheck(@RequestBody KeyValue keyValue) throws ExecutionException, InterruptedException {
        return keyValueService.updateKeyValue(keyValue);
    }
    @DeleteMapping(value = "/delete")
    public String deleteCheck(@RequestParam String id) {
        return keyValueService.deleteKeyValue(id);
    }

}
