package com.jabai.campustrack.Services;

import com.jabai.campustrack.Models.Health;

public class HealthService {
  public Health getHealthy() {
    return new Health("Healthy");
  }
}