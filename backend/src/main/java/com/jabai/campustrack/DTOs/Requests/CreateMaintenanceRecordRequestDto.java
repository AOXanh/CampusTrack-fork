package com.jabai.campustrack.DTOs.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import com.jabai.campustrack.Models.Enums.MaintenanceRecordStatus;


public class CreateMaintenanceRecordRequestDto {
    
    @NotNull(message = "Incident ID is required")
    private Long incidentId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotBlank(message = "Action is required")
    private String action;
    
    private String remarks;
    
    private String status;
    
    private LocalDateTime completedAt;

    // Getters and Setters
    public Long getIncidentId() { return incidentId; }
    public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}