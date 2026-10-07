package com.jabai.campustrack.DTOs.Requests;

import com.jabai.campustrack.Models.Enums.IncidentCategory;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record CreateIncidentRequestDto(
    @NotNull(message = "Room ID is required.") @Positive Long roomId,
    @Positive Long assetId,
    // Temporary input until middleware supplies the authenticated reporter's ID.
    @NotNull(message = "Reporter ID is required.") @Positive Long reportedById,
    @NotNull(message = "Category is required.") IncidentCategory category,
    @NotBlank(message = "Description is required.") @Size(max = 16000) String description,
    @NotNull(message = "Safety hazard information is required.") Boolean safetyHazard,
    @NotNull(message = "Operational impact information is required.") Boolean operationalImpact,
    @Positive Integer affectedArea,
    // Nullable database fields may be omitted from the request.
    @Positive Long assignedToId,
    LocalDateTime evaluatedAt,
    LocalDateTime resolvedAt,
    LocalDateTime closedAt
) {
    public CreateIncidentRequestDto {
        if (affectedArea == null) affectedArea = 1;
    }
}
