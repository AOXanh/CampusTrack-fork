package com.jabai.campustrack.DTOs.Requests;

import com.jabai.campustrack.Models.Enums.MaintenanceRecordStatus;

import java.time.LocalDateTime;

public class SearchMaintenanceRecordRequestDto {
  private final Long incidentId;
  private final Long userId;
  private final String action;
  private final String remarks;
  private final MaintenanceRecordStatus status;
  private final LocalDateTime startDate;
  private final LocalDateTime endDate;

  public SearchMaintenanceRecordRequestDto(
          Long incidentId,
          Long userId,
          String action,
          String remarks,
          MaintenanceRecordStatus status,
          LocalDateTime startDate,
          LocalDateTime endDate
  ) {
    this.incidentId = incidentId;
    this.userId = userId;
    this.action = action;
    this.remarks = remarks;
    this.status = status;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  // Getters
  public Long getIncidentId() { return incidentId; }
  public Long getUserId() { return userId; }
  public String getAction() { return action; }
  public String getRemarks() { return remarks; }
  public MaintenanceRecordStatus getStatus() { return status; }
  public LocalDateTime getStartDate() { return startDate; }
  public LocalDateTime getEndDate() { return endDate; }
}
