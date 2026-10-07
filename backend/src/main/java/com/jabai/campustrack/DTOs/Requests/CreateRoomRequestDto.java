package com.jabai.campustrack.DTOs.Requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.jabai.campustrack.Models.Enums.RoomCriticality;
import com.jabai.campustrack.Models.Enums.RoomType;
import jakarta.validation.constraints.Size;

public class CreateRoomRequestDto {
    @NotNull(message = "Building is required.")
    private final Long buildingId;

    @NotEmpty(message = "Room number is required.")
    @Size(max = 50, message = "Room number must be 0 to 50 characters only.")
    private final String roomNumber;

    @NotNull(message = "Capacity is required.")
    @Positive(message = "Room capacity must be positive.")
    private final Integer capacity;

    @NotNull(message = "Room Type is required.")
    private final RoomType roomType;

    @NotNull(message = "Criticality is required.")
    private final RoomCriticality criticality;
    
    public CreateRoomRequestDto(Long buildingId, String roomNumber, Integer capacity, RoomType roomType, RoomCriticality criticality) {
        this.buildingId = buildingId;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.roomType = roomType;
        this.criticality = criticality;
    }

    public Long getBuildingId() { return buildingId; }
    public Integer getCapacity() { return capacity; }
    public RoomCriticality getCriticality() { return criticality; }
    public String getRoomNumber() { return roomNumber; }
    public RoomType getRoomType() { return roomType; }
}
