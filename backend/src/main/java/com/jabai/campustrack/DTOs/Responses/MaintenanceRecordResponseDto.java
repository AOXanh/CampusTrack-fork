package com.jabai.campustrack.DTOs.Responses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import com.jabai.campustrack.Models.Enums.MaintenanceRecordStatus;

public class MaintenanceRecordResponseDto {
    
    private Long id;
    
    @NotNull(message = "Incident ID is required")
    private Long incidentId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotBlank(message = "Action is required")
    private String action;
    
    private String remarks;
    
    private MaintenanceRecordStatus status;
    
    private LocalDateTime completedAt;
    
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIncidentId() { return incidentId; }
    public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public MaintenanceRecordStatus getStatus() { return status; }
    public void setStatus(MaintenanceRecordStatus status) { this.status = status; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}