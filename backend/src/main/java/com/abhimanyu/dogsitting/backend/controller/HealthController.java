package com.abhimanyu.dogsitting.backend.controller;

import com.abhimanyu.dogsitting.backend.dto.HealthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public HealthResponse health(){
        return new HealthResponse("UP", Instant.now());
    }

}
