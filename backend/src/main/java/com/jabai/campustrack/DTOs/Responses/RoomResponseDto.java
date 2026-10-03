package com.jabai.campustrack.DTOs.Responses;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jabai.campustrack.Models.Building;
import com.jabai.campustrack.Models.Enums.RoomCriticality;
import com.jabai.campustrack.Models.Enums.RoomType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomResponseDto {

    private final long id;
    private final LocalDateTime createdAt;
    private final BuildingResponseDto building;
    private final String roomNumber;
    private final int capacity;
    private final RoomType roomType;
    private final RoomCriticality roomCriticality;

    public RoomResponseDto(long id, LocalDateTime createdAt, Building building, String roomNumber, int capacity, RoomType roomType, RoomCriticality roomCriticality) {
        this.id = id;
        this.createdAt = createdAt;
        if (building != null) {
            this.building = new BuildingResponseDto(building.getId(), building.getName(), building.getCreatedAt()); // removes rooms
        } else {
            this.building = null;
        }
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.roomType = roomType;
        this.roomCriticality = roomCriticality;
    }

    public long getId() {
        return id;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public BuildingResponseDto getBuilding() {
        return building;
    }
    public String getRoomNumber() {
        return roomNumber;
    }
    public int getCapacity() {
        return capacity;
    }
    public RoomType getRoomType() {
        return roomType;
    }
    public RoomCriticality getRoomCriticality() {
        return roomCriticality;
    }

}
