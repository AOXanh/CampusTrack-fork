package com.jabai.campustrack.DTOs.Requests;

import jakarta.validation.constraints.NotNull;

public class GetRoomsRequestDto {
    @NotNull(message="Building is required.")
    private final Long buildingID; 

    public GetRoomsRequestDto(Long buildingID) {
        this.buildingID = buildingID;
    }

   public Long getBuildingID() {
       return buildingID;
   }
}
