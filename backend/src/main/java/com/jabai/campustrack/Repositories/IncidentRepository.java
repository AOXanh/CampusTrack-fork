package com.jabai.campustrack.Repositories;

import com.jabai.campustrack.Models.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
  boolean existsByIncidentNumber(String incidentNumber);
}
