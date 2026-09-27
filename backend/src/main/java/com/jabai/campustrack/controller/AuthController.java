package com.jabai.campustrack.controller;

import com.jabai.campustrack.model.Health;
import com.jabai.campustrack.services.HealthService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final HealthService healthService = new HealthService();

  @GetMapping("/health")
  public Health health() {
    return  healthService.getHealthy();
  }
}
