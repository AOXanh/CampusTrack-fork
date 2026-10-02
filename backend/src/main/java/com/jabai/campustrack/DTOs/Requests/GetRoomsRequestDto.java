package com.jabai.campustrack.DTOs.Requests;

import com.jabai.campustrack.Models.Enums.RoomCriticality;
import com.jabai.campustrack.Models.Enums.RoomType;

import jakarta.validation.constraints.NotNull;

public class GetRoomsRequestDto {
    @NotNull(message="Building is required.")
    private final Long buildingID; 
    
    private final RoomCriticality criticality;
    
    private final RoomType roomType;

    public GetRoomsRequestDto(Long buildingID, RoomCriticality criticality, RoomType roomType) {
        this.buildingID = buildingID;
        this.criticality = criticality;
        this.roomType = roomType;
    }

   public Long getBuildingID() {
       return buildingID;
   }

   public RoomCriticality getCriticality() {
       return criticality;
   }

   public RoomType getRoomType() {
       return roomType;
   }
}
