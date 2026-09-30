package com.jabai.campustrack.DTOs.Responses;

import com.jabai.campustrack.Models.Enums.IncidentCategory;
import com.jabai.campustrack.Models.Enums.IncidentPriority;
import com.jabai.campustrack.Models.Enums.IncidentStatus;
import java.time.LocalDateTime;

public record IncidentResponseDto(
    long id, String incidentNumber, long roomId, Long assetId,
    long reportedById, Long assignedToId, IncidentCategory category, String description,
    boolean safetyHazard, boolean operationalImpact, int affectedArea,
    IncidentPriority priority, int priorityScore, IncidentStatus status,
    LocalDateTime createdAt, LocalDateTime evaluatedAt,
    LocalDateTime resolvedAt, LocalDateTime closedAt
) { }
