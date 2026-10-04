package com.jabai.campustrack.DTOs.Requests;

import com.jabai.campustrack.Models.Enums.AssetCategory;
import com.jabai.campustrack.Models.Enums.AssetStatus;
import com.jabai.campustrack.Models.Enums.AssetCondition;
import com.jabai.campustrack.Models.Enums.AssetCriticality;

public record UpdateAssetRequestDto(
        Long roomId,
        String name,
        String brand,
        String model,
        String serialNumber,
        AssetCategory category,
        AssetStatus status,
        AssetCondition condition,
        AssetCriticality criticality
) {
}