package com.abhimanyu.dogsitting.backend.dto;

import java.time.Instant;

public record HealthResponse(String status, Instant timestamp) { }
