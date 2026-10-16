package com.jabai.campustrack.Repositories;

import com.jabai.campustrack.Models.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IncidentRepository extends JpaRepository<Incident, Long>, JpaSpecificationExecutor<Incident> {
  boolean existsByIncidentNumber(String incidentNumber);
}
