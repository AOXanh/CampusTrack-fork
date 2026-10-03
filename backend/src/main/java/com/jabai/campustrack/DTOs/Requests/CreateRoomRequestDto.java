package com.jabai.campustrack.DTOs.Requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.jabai.campustrack.Models.Enums.RoomCriticality;
import com.jabai.campustrack.Models.Enums.RoomType;

public class CreateRoomRequestDto {
    @NotNull(message="Building is required.")
    private final Long building_id; 

    @NotEmpty(message="Room number is required.")
    private final String roomNumber;

    @NotNull(message="Capacity is required.")
    @Positive 
    private final Integer capacity;

    @NotNull(message="Room Type is required.")
    private final RoomType roomType;

    @NotNull(message="Criticality is required.")
    private final RoomCriticality criticality;
    
    public CreateRoomRequestDto(Long building_id, String roomNumber, Integer capacity, RoomType roomType, RoomCriticality criticality) {
        this.building_id = building_id;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.roomType = roomType;
        this.criticality = criticality;
    }

    public long getBuilding_id() {
        return building_id;
    }
    public int getCapacity() {
        return capacity;
    }
    public RoomCriticality getCriticality() {
        return criticality;
    }
    public String getRoomNumber() {
        return roomNumber;
    }
    public RoomType getRoomType() {
        return roomType;
    }
}
