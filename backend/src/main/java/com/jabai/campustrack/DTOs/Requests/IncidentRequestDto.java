package com.jabai.campustrack.DTOs.Requests;

import com.jabai.campustrack.Models.Enums.IncidentCategory;
import jakarta.validation.constraints.*;

public record IncidentRequestDto(
    @Positive Long roomId,
    @Positive Long assetId,
    @NotNull(message = "Category is required.") IncidentCategory category,
    @NotBlank(message = "Description is required.") String description,
    @NotNull(message = "Safety hazard information is required.") Boolean safetyHazard,
    @NotNull(message = "Operational impact information is required.") Boolean operationalImpact,
    @Positive(message = "Affected area must be positive.") Integer affectedArea
) {
    public IncidentRequestDto {
        if (affectedArea == null) affectedArea = 1;
    }

    // NFC reports can derive their room from the asset; manual reports require a room.
    @AssertTrue(message = "A room or asset is required.")
    public boolean isLocationProvided() { return roomId != null || assetId != null; }
}
