package com.jabai.campustrack.DTOs.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import com.jabai.campustrack.Models.Enums.MaintenanceRecordStatus;


public class CreateMaintenanceRecordRequestDto {
    @NotNull(message = "Incident ID is required.")
    private final Long incidentId;
    
    @NotNull(message = "User ID is required.")
    private final Long userId;
    
    @NotBlank(message = "Action is required.")
    private final String action;

    @NotNull(message = "Status is required.")
    private final MaintenanceRecordStatus status;
    
    private final String remarks;

    private final LocalDateTime completedAt;

    public CreateMaintenanceRecordRequestDto(
            Long incidentId,
            Long userId,
            String action,
            MaintenanceRecordStatus status,
            String remarks,
            LocalDateTime completedAt
    ) {
        this.incidentId = incidentId;
        this.userId = userId;
        this.action = action;
        this.status = status;
        this.remarks = remarks;
        this.completedAt = completedAt;
    }

    // Getters
    public Long getIncidentId() { return incidentId; }
    public Long getUserId() { return userId; }
    public String getAction() { return action; }
    public String getRemarks() { return remarks; }
    public MaintenanceRecordStatus getStatus() { return status; }
    public LocalDateTime getCompletedAt() { return completedAt; }
}