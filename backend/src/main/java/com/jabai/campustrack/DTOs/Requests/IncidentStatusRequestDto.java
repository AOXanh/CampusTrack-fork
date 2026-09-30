package com.jabai.campustrack.DTOs.Requests;

import com.jabai.campustrack.Models.Enums.IncidentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record IncidentStatusRequestDto(
    @NotNull(message = "Status is required.") IncidentStatus status,
    @Positive Long assignedToId
) { }
