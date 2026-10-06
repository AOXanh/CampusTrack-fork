package com.jabai.campustrack.DTOs.Responses;

import com.jabai.campustrack.Models.Enums.IncidentCategory;
import com.jabai.campustrack.Models.Enums.IncidentStatus;

import java.time.LocalDateTime;

public class IncidentResponseDto {
  private Long assetId;
  private Long assignedTo;
  private final Long id;
  private final LocalDateTime createdAt;
  private final LocalDateTime evaluatedAt;
  private final LocalDateTime resolvedAt;
  private final LocalDateTime closedAt;
  private final Long roomId;
  private final Long reportedBy;
  private final String incidentNumber;
  private final String description;
  private final Boolean safetyHazard;
  private final Boolean operationalImpact;
  private final Integer priorityScore;
  private final IncidentCategory category;
  private final IncidentStatus status;

  public IncidentResponseDto(
          Long id,
          LocalDateTime createdAt,
          LocalDateTime evaluatedAt,
          LocalDateTime resolvedAt,
          LocalDateTime closedAt,
          Long roomId,
          Long reportedBy,
          String incidentNumber,
          String description,
          Boolean safetyHazard,
          Boolean operationalImpact,
          Integer priorityScore,
          IncidentCategory category,
          IncidentStatus status
  ) {
    this.id = id;
    this.createdAt = createdAt;
    this.evaluatedAt = evaluatedAt;
    this.resolvedAt = resolvedAt;
    this.closedAt = closedAt;
    this.roomId = roomId;
    this.reportedBy = reportedBy;
    this.incidentNumber = incidentNumber;
    this.description = description;
    this.safetyHazard = safetyHazard;
    this.operationalImpact = operationalImpact;
    this.priorityScore = priorityScore;
    this.category = category;
    this.status = status;
  }

  // Getters
  public Long getId() { return id; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public LocalDateTime getEvaluatedAt() { return evaluatedAt; }
  public LocalDateTime getResolvedAt() { return resolvedAt; }
  public LocalDateTime getClosedAt() { return closedAt; }
  public Long getAssetId() { return assetId; }
  public Long getRoomId() { return roomId; }
  public Long getReportedBy() { return reportedBy; }
  public Long getAssignedTo() { return assignedTo; }
  public String getIncidentNumber() { return incidentNumber; }
  public String getDescription() { return description; }
  public Boolean getSafetyHazard() { return safetyHazard; }
  public Boolean getOperationalImpact() { return operationalImpact; }
  public Integer getPriorityScore() { return priorityScore; }
  public IncidentCategory getCategory() { return category; }
  public IncidentStatus getStatus() { return status; }

  // Setters
  public void setAssetId(Long assetId) { this.assetId = assetId; }
  public void setAssignedTo(Long assignedTo) { this.assignedTo = assignedTo; }
}
