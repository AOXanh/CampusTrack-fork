package com.jabai.campustrack.Controllers;

import com.jabai.campustrack.Models.Health;
import com.jabai.campustrack.Services.HealthService;
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
