package com.jabai.campustrack.DTOs.Requests;

import com.jabai.campustrack.Models.Enums.RoomCriticality;
import com.jabai.campustrack.Models.Enums.RoomType;

public class SearchRoomRequestDto {
    private final Long buildingId;
    private final String roomNumber;
    private final Integer capacity;
    private final RoomType roomType;
    private final RoomCriticality criticality;

    public SearchRoomRequestDto(
            Long buildingId,
            String roomNumber,
            Integer capacity,
            RoomType roomType,
            RoomCriticality criticality
    ) {
        this.buildingId = buildingId;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.roomType = roomType;
        this.criticality = criticality;
    }

    // Getters
    public Long getBuildingId() { return buildingId; }
    public String getRoomNumber() { return roomNumber; }
    public Integer getCapacity() { return capacity; }
    public RoomType getRoomType() { return roomType; }
    public RoomCriticality getCriticality() { return criticality; }
}