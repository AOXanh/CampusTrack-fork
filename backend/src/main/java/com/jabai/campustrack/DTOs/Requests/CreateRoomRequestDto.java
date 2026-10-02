package com.jabai.campustrack.DTOs.Requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.jabai.campustrack.Models.Enums.RoomCriticality;
import com.jabai.campustrack.Models.Enums.RoomType;
import com.jabai.campustrack.Models.Building;

public class CreateRoomRequestDto {
    @NotNull(message="Building is required.")
    private final Building building; 

    @NotEmpty(message="Room number is required.")
    private final String roomNumber;

    @NotNull(message="Capacity is required.")
    @Positive 
    private final int capacity;

    @NotNull(message="Room Type is required.")
    private final RoomType roomType;

    @NotNull(message="Criticality is required.")
    private final RoomCriticality criticality;
    
    public CreateRoomRequestDto(Building building, String roomNumber, int capacity, RoomType roomType, RoomCriticality criticality) {
        this.building = building;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.roomType = roomType;
        this.criticality = criticality;
    }

    public Building getBuilding() {
        return building;
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
