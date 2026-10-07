package com.jabai.campustrack.DTOs.Requests;

import com.jabai.campustrack.Models.Enums.MaintenanceRecordStatus;

import java.time.LocalDateTime;

public class SearchMaintenanceRecordRequestDto {
  private final Long incidentId;
  private final Long userId;
  private final String action;
  private final String remarks;
  private final MaintenanceRecordStatus status;
  private final LocalDateTime completedFrom;
  private final LocalDateTime completedTo;

  public SearchMaintenanceRecordRequestDto(
          Long incidentId,
          Long userId,
          String action,
          String remarks,
          MaintenanceRecordStatus status,
          LocalDateTime completedFrom,
          LocalDateTime completedTo
  ) {
    this.incidentId = incidentId;
    this.userId = userId;
    this.action = action;
    this.remarks = remarks;
    this.status = status;
    this.completedFrom = completedFrom;
    this.completedTo = completedTo;
  }

  // Getters
  public Long getIncidentId() { return incidentId; }
  public Long getUserId() { return userId; }
  public String getAction() { return action; }
  public String getRemarks() { return remarks; }
  public MaintenanceRecordStatus getStatus() { return status; }
  public LocalDateTime getCompletedFrom() { return completedFrom; }
  public LocalDateTime getCompletedTo() { return completedTo; }
}
