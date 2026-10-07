package com.jabai.campustrack.DTOs.Requests;

import java.time.LocalDateTime;
import com.jabai.campustrack.Models.Enums.MaintenanceRecordStatus;
import jakarta.validation.constraints.Size;


public class UpdateMaintenanceRecordRequestDto {
    private final Long incidentId;
    private final Long userId;

    @Size(max = 100, message = "Action must be 0 to 100 characters only.")
    private final String action;

    private final String remarks;
    private final MaintenanceRecordStatus status;
    private final LocalDateTime completedAt;

    public UpdateMaintenanceRecordRequestDto(
            Long incidentId,
            Long userId,
            String action,
            String remarks,
            MaintenanceRecordStatus status,
            LocalDateTime completedAt
    ) {
        this.incidentId = incidentId;
        this.userId = userId;
        this.action = action;
        this.remarks = remarks;
        this.status = status;
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