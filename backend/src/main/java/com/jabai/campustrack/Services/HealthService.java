package com.jabai.campustrack.services;

import com.jabai.campustrack.model.Health;

public class HealthService {
  public Health getHealthy() {
    return new Health("Healthy");
  }
}