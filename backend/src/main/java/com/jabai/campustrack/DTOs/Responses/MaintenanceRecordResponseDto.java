package com.jabai.campustrack.DTOs.Responses;

import java.time.LocalDateTime;
import com.jabai.campustrack.Models.Enums.MaintenanceRecordStatus;

public class MaintenanceRecordResponseDto {
    private final Long id;
    private final IncidentResponseDto incident;
    private final UserProfileResponseDto user;
    private final String action;
    private final String remarks;
    private final MaintenanceRecordStatus status;
    private final LocalDateTime completedAt;
    private final LocalDateTime createdAt;

    public MaintenanceRecordResponseDto(
            Long id,
            IncidentResponseDto incident,
            UserProfileResponseDto user,
            String action,
            String remarks,
            MaintenanceRecordStatus status,
            LocalDateTime completedAt,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.incident = incident;
        this.user = user;
        this.action = action;
        this.remarks = remarks;
        this.status = status;
        this.completedAt = completedAt;
        this.createdAt = createdAt;
    }

    // Getters
    public Long getId() { return id; }
    public IncidentResponseDto getIncident() { return incident; }
    public UserProfileResponseDto getUser() { return user; }
    public String getAction() { return action; }
    public String getRemarks() { return remarks; }
    public MaintenanceRecordStatus getStatus() { return status; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}